package com.github.j4c62.pms.property.infrastructure.provider.kafka;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.shared.AggregateFixture;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EnableKafka
@EmbeddedKafka(
    partitions = 1,
    topics = {"property.created", "property.updated"})
@TestPropertySource(
    properties = {
      "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
      "spring.kafka.streams.bootstrap-servers=${spring.embedded.kafka.brokers}",
      "spring.kafka.streams.application-id=property-service",
      "spring.kafka.streams.state-dir: ${java.io.tmpdir}/kafka-streams-integration-test",
      "grpc.server.port=-1",
      "application.booking.kafka.store-name=kafka-streams-integration-test"
    })
@Import({AggregateFixture.class, PropertyTopology.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KafkaIntegrationTest {
  @Value("${application.property.kafka.store-name}")
  String storeName;

  @Autowired private StreamBridge streamBridge;
  @Autowired private InteractiveQueryService queryService;
  @Autowired private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

  @Test
  void givenPropertyEventsWhenProducedThenShouldBeConsumedSuccessfully(
      @Autowired @Qualifier("propertyCreatedEvent") PropertyEvent propertyCreatedEvent,
      @Autowired @Qualifier("propertyUpdatedEvent") PropertyEvent propertyUpdatedEvent) {
    await()
        .atMost(Duration.ofSeconds(30))
        .untilAsserted(
            () ->
                assertThat(requireNonNull(streamsBuilderFactoryBean.getKafkaStreams()).state())
                    .isEqualTo(KafkaStreams.State.RUNNING));

    streamBridge.send("propertyEventSupplier-out-0", propertyCreatedEvent);
    streamBridge.send("propertyEventSupplier-out-0", propertyUpdatedEvent);

    await()
        .atMost(Duration.ofSeconds(20))
        .untilAsserted(() -> assertThat(getStoredEvents()).hasSizeGreaterThan(0));
  }

  public List<String> getStoredEvents() {
    ReadOnlyKeyValueStore<Id, PropertyEvents> store =
        queryService.getQueryableStore(storeName, QueryableStoreTypes.keyValueStore());

    try (var iterator = store.all()) {
      List<String> results = new ArrayList<>();
      while (iterator.hasNext()) {
        var kv = iterator.next();
        results.add(kv.value.toString());
      }
      return results;
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @AfterAll
  void cleanUp() throws IOException {
    var kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
    if (kafkaStreams != null) {
      kafkaStreams.close(Duration.ofSeconds(10));
    }
    var stateDir = System.getProperty("java.io.tmpdir") + "/kafka-streams-integration-test";

    var dir = Paths.get(stateDir);
    if (Files.exists(dir)) {
      try (var stream = Files.walk(dir)) {
        stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
      }
    }
  }
}
