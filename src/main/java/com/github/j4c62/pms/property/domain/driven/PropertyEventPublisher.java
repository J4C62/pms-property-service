package com.github.j4c62.pms.property.domain.driven;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;

/**
 * Domain interface for publishing property events.
 *
 * <p>Implementations of this interface are responsible for publishing property events to external systems
 * such as message brokers or event buses.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@FunctionalInterface
public interface PropertyEventPublisher {
  /**
   * Publishes a property event to an external system.
   *
   * @param propertyEvent the property event to publish
   * @since 2025-09-26
   */
  void publish(PropertyEvent propertyEvent);
}
