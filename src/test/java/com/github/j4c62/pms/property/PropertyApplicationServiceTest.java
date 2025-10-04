package com.github.j4c62.pms.property;

import static com.github.j4c62.pms.property.infrastructure.provider.grpc.Capacity.newBuilder;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyCreatedEvent;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyUpdatedEvent;
import com.github.j4c62.pms.property.domain.aggregate.vo.Facility;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.driven.PropertyEventPublisher;
import com.github.j4c62.pms.property.domain.driven.PropertyEventStore;
import com.github.j4c62.pms.property.infrastructure.adapter.driven.KafkaProducerAdapter;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.CreatePropertyRequest;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.Data;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.Location;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.Price;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.PropertyServiceGrpc;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.UpdatePropertyRequest;
import com.github.j4c62.pms.property.shared.AggregateFixture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.KafkaStreams;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@ImportTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
    properties = {
      "grpc.server.inProcessName=flowTest",
      "grpc.server.port=-1",
      "grpc.client.inProcess.address=in-process:flowTest"
    })
@Import({AggregateFixture.class, KafkaProducerAdapter.class})
class PropertyApplicationServiceTest {

  private static final KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"))
          .withStartupTimeout(Duration.ofSeconds(60));
  private static String createdPropertyId;

  @GrpcClient("inProcess")
  private PropertyServiceGrpc.PropertyServiceBlockingStub propertyServiceBlockingStub;

  @Autowired private PropertyEventStore propertyEventStore;
  @Autowired private StreamsBuilderFactoryBean streamsBuilderFactoryBean;
  @Autowired private PropertyEventPublisher propertyEventPublisher;

  @BeforeAll
  static void setUpKafka() throws Exception {
    kafka.start();
    var store = "%s/property-events-store-dev".formatted(System.getProperty("java.io.tmpdir"));
    System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
    System.setProperty("spring.kafka.streams.bootstrap-servers", kafka.getBootstrapServers());
    System.setProperty("spring.kafka.streams.state-dir", store);
    System.setProperty("application.property.kafka.store-name", "property-events-store-dev");

    try (AdminClient adminClient =
        AdminClient.create(
            Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers()))) {

      List<NewTopic> topics =
          List.of(
              new NewTopic("property.created", 1, (short) 1),
              new NewTopic("property.updated", 1, (short) 1));

      adminClient.createTopics(topics).all().get(10, TimeUnit.SECONDS);
    }
  }

  @Test
  @Order(1)
  void contextLoads(ApplicationContext context) {
    assertThat(context).as("Application context should be initialized").isNotNull();
    await()
        .atMost(Duration.ofSeconds(20))
        .untilAsserted(
            () -> {
              var kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
              assertThat(requireNonNull(kafkaStreams).state())
                  .as("Kafka Streams should be in RUNNING state")
                  .isEqualTo(KafkaStreams.State.RUNNING);
            });
  }

  @Test
  @Order(2)
  void givenGuestBookingWhenGuestDoesTheBookingThenBookingIsDoneAndGuestIsNotified(
      @Autowired com.github.j4c62.pms.property.domain.aggregate.vo.Data data) {
    var createPropertyRequest =
        CreatePropertyRequest.newBuilder()
            .setData(
                Data.newBuilder()
                    .setName(data.name().value())
                    .setType(data.type().name())
                    .setCapacity(
                        newBuilder()
                            .setMax(data.capacity().max())
                            .setMin(data.capacity().min())
                            .build())
                    .setSize(data.size().meters())
                    .setLayout(data.layout().name())
                    .setIsolation(data.isolation().name())
                    .setPrivateEntrance(data.privateEntrance())
                    .setFloor(data.floor().level())
                    .setAccessibility(data.accessibility().score())
                    .addAllFacilities(
                        data.facilities().items().stream().map(Facility::name).toList())
                    .setLocation(
                        Location.newBuilder()
                            .setName(data.location().name())
                            .setType(data.location().type().name())
                            .build())
                    .setPrice(
                        Price.newBuilder()
                            .setAmount(data.price().amount())
                            .setCurrency(data.price().currency().getCurrencyCode())
                            .build())
                    .build())
            .setStatus("ACTIVE")
            .build();

    var result = propertyServiceBlockingStub.createProperty(createPropertyRequest);
    createdPropertyId = result.getId();
    assertThat(createdPropertyId).as("Created booking ID should not be null").isNotNull();
    await()
        .atMost(Duration.ofSeconds(40))
        .untilAsserted(
            () -> {
              var events =
                  propertyEventStore.getEventsForProperty(
                      Id.of(UUID.fromString(createdPropertyId)));
              assertThat(events).as("Events should not be null for created property").isNotNull();
              assertThat(events.events())
                  .as("First event should be PropertyCreatedEvent")
                  .element(0)
                  .isExactlyInstanceOf(PropertyCreatedEvent.class);
            });
  }

  @Test
  @Order(3)
  void givenGuestUpdateDatesWhenGuestUpdateDatesThenBookingIsUpdateAndGuestIsNotified(
      @Autowired com.github.j4c62.pms.property.domain.aggregate.vo.Data data) {
    await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(
            () -> {
              var events =
                  propertyEventStore.getEventsForProperty(
                      Id.of(UUID.fromString(createdPropertyId)));
              assertThat(events).as("Events should not be null before updating").isNotNull();
              assertThat(events.events())
                  .as("Events list should not be empty before updating")
                  .isNotEmpty();
            });

    var updatePropertyRequest =
        UpdatePropertyRequest.newBuilder()
            .setId(createdPropertyId)
            .setData(
                Data.newBuilder()
                    .setName(data.name().value())
                    .setType(data.type().name())
                    .setCapacity(
                        newBuilder()
                            .setMax(data.capacity().max())
                            .setMin(data.capacity().min())
                            .build())
                    .setSize(data.size().meters())
                    .setLayout(data.layout().name())
                    .setIsolation(data.isolation().name())
                    .setPrivateEntrance(data.privateEntrance())
                    .setFloor(data.floor().level())
                    .setAccessibility(data.accessibility().score())
                    .addAllFacilities(
                        data.facilities().items().stream().map(Facility::name).toList())
                    .setLocation(
                        Location.newBuilder()
                            .setName(data.location().name())
                            .setType(data.location().type().name())
                            .build())
                    .setPrice(
                        Price.newBuilder()
                            .setAmount(data.price().amount())
                            .setCurrency(data.price().currency().getCurrencyCode())
                            .build())
                    .build())
            .setStatus("INACTIVE")
            .build();

    var result = propertyServiceBlockingStub.updateProperty(updatePropertyRequest);

    assertThat(result.getId())
        .as("Updated property ID should match the original")
        .isEqualTo(createdPropertyId);
    await()
        .atMost(Duration.ofSeconds(40))
        .untilAsserted(
            () -> {
              var events =
                  propertyEventStore.getEventsForProperty(
                      Id.of(UUID.fromString(createdPropertyId)));
              assertThat(events).as("Events should not be null after updating").isNotNull();
              assertThat(events.events())
                  .element(1)
                  .as("Second event should be PropertyUpdatedEvent")
                  .isExactlyInstanceOf(PropertyUpdatedEvent.class);
            });
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @AfterAll
  static void cleanUp() throws IOException {
    var stateDir = System.getProperty("java.io.tmpdir") + "/property-events-store-dev";

    var dir = Paths.get(stateDir);
    if (Files.exists(dir)) {
      try (var stream = Files.walk(dir)) {
        stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
      }
    }
  }
}
