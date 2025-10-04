package com.github.j4c62.pms.property.infrastructure.adapter.driven;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.driven.PropertyEventPublisher;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

/**
 * Kafka producer adapter for publishing property events.
 *
 * <p>This service bridges the domain event publisher interface to the Spring Cloud StreamBridge,
 * sending property events to the appropriate Kafka topic.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerAdapter implements PropertyEventPublisher {
  private final StreamBridge streamBridge;

  /**
   * Publishes a property event to the Kafka topic using StreamBridge.
   *
   * @param propertyEvent the property event to publish
   * @since 2025-09-26
   */
  @Override
  public void publish(@NonNull PropertyEvent propertyEvent) {
    var isSend = streamBridge.send("propertyEventSupplier-out-0", propertyEvent);
  }
}
