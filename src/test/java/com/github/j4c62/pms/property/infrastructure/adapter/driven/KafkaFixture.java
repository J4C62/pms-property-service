package com.github.j4c62.pms.property.infrastructure.adapter.driven;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.shared.AggregateFixture;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({JacksonAutoConfiguration.class, AggregateFixture.class})
public class KafkaFixture {
  @TestComponent
  public record SetUpFixtureIntegration(
      @Qualifier("propertyCreatedEvent") PropertyEvent propertyCreatedEvent,
      @Qualifier("propertyUpdatedEvent") PropertyEvent propertyUpdatedEvent) {}
}
