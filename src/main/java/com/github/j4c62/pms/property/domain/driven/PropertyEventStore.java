package com.github.j4c62.pms.property.domain.driven;

import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;

/**
 * Domain interface for retrieving property event histories from a store.
 *
 * <p>Implementations of this interface provide access to the event stream for a given property ID,
 * typically from an event store or stateful backend.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@FunctionalInterface
public interface PropertyEventStore {
  /**
   * Retrieves the event history for a property by its ID.
   *
   * @param id the property ID
   * @return the property events for the given ID
   * @since 2025-09-26
   */
  PropertyEvents getEventsForProperty(Id id);
}
