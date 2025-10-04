package com.github.j4c62.pms.property.domain.aggregate.vo;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import java.util.List;
import java.util.stream.Stream;

/**
 * Value object representing a collection of property events.
 *
 * <p>This record encapsulates a list of {@link PropertyEvent} objects and provides utility methods
 * for event stream manipulation, such as appending events, creating empty collections, and replaying
 * events on a {@link PropertyAggregate} to restore or update its state.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record PropertyEvents(List<PropertyEvent> events) {
  /**
   * Constructs a PropertyEvents record, ensuring the event list is never null and is immutable.
   *
   * @param events the list of property events (may be null)
   * @since 2025-09-26
   */
  public PropertyEvents {
    events = events == null ? List.of() : List.copyOf(events);
  }

  /**
   * Creates a PropertyEvents instance from a list of events.
   *
   * @param events the list of property events
   * @return a new PropertyEvents instance
   * @since 2025-09-26
   */
  public static PropertyEvents of(List<PropertyEvent> events) {
    return new PropertyEvents(events);
  }

  /**
   * Returns an empty PropertyEvents instance.
   *
   * @return an empty PropertyEvents
   * @since 2025-09-26
   */
  public static PropertyEvents empty() {
    return new PropertyEvents(List.of());
  }

  /**
   * Appends a property event to this collection, returning a new PropertyEvents instance.
   *
   * @param event the property event to append
   * @return a new PropertyEvents with the event added
   * @since 2025-09-26
   */
  public PropertyEvents append(PropertyEvent event) {
    return new PropertyEvents(Stream.concat(events.stream(), Stream.of(event)).toList());
  }

  /**
   * Replays all events in this collection on the given PropertyAggregate, applying each event in order.
   *
   * @param base the base PropertyAggregate to apply events to
   * @return a Result containing the updated PropertyAggregate, or an error message if any event application fails
   * @since 2025-09-26
   */
  public Result<PropertyAggregate, String> replayOn(PropertyAggregate base) {
    PropertyAggregate current = base;
    for (PropertyEvent event : events) {
      Result<PropertyAggregate, String> result = event.applyTo(current);
      if (result.isErr()) {
        return result;
      }
      current = result.unwrap();
    }
    return Result.ok(current);
  }
}
