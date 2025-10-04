package com.github.j4c62.pms.property.infrastructure.adapter.driver;

import static com.github.j4c62.pms.property.infrastructure.provider.grpc.Capacity.newBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.j4c62.pms.property.domain.aggregate.vo.Facility;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.CreatePropertyRequest;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.Data;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.Location;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.Price;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.PropertyServiceGrpc;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.UpdatePropertyRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(GrpcFixture.class)
@TestPropertySource(
    properties = {
      "grpc.server.inProcessName=test",
      "grpc.server.port=-1",
      "grpc.client.inProcess.address=in-process:test",
    })
class GrpcControllerAdapterIntegrationTest {

  @GrpcClient("inProcess")
  private PropertyServiceGrpc.PropertyServiceBlockingStub propertyServiceBlockingStub;

  @Test
  void givenValidCreatePropertyRequestWhenCreatePropertyThenPropertyShouldBeCreatedSuccessfully(
      @Autowired Id id, @Autowired com.github.j4c62.pms.property.domain.aggregate.vo.Data data) {

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

    assertThat(result.getId())
        .as("Property ID should not be null when a booking is successfully created")
        .isNotNull();

    assertThat(result.getStatus())
        .as("Expected property status to be 'ACTIVE' after creation")
        .isEqualTo(Status.ACTIVE.name());
  }

  @Test
  void givenValidUpdatePropertyRequestWhenUpdatePropertyThenPropertyShouldBeUpdatedSuccessfully(
      @Autowired Id id, @Autowired com.github.j4c62.pms.property.domain.aggregate.vo.Data data) {

    var updatePropertyRequest =
        UpdatePropertyRequest.newBuilder()
            .setId(id.value().toString())
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
        .as("Property ID should not be null when a booking is successfully updated")
        .isNotNull();

    assertThat(result.getStatus())
        .as("Expected property status to be 'INACTIVE' after updated")
        .isEqualTo(Status.INACTIVE.name());
  }
}
