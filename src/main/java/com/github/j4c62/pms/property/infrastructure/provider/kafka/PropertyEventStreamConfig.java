package com.github.j4c62.pms.property.infrastructure.provider.kafka;

import static org.springframework.messaging.support.MessageBuilder.withPayload;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * Configuration for property event stream message building.
 *
 * <p>This class provides a bean for converting PropertyEvent objects into Spring Messages with
 * CloudEvents headers for downstream processing.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Configuration
public class PropertyEventStreamConfig {

  /**
   * Supplies a function that wraps PropertyEvent objects as Spring Messages with CloudEvents
   * headers.
   *
   * @return a function that builds a Message from a PropertyEvent.
   * @since 2025-09-26
   */
  @Bean
  public Function<PropertyEvent, Message<PropertyEvent>> propertyEventSupplier() {
    return propertyEvent ->
        withPayload(propertyEvent)
            .setHeader("ce-id", UUID.randomUUID().toString())
            .setHeader("ce-type", propertyEvent.eventType().getEventType())
            .setHeader("ce-source", "property-service")
            .setHeader("ce-specversion", "1.0")
            .setHeader("ce-time", OffsetDateTime.now().toString())
            .setHeader(MessageHeaders.CONTENT_TYPE, "application/json")
            .build();
  }
}
