package com.github.j4c62.pms.property.domain.aggregate.vo;

/**
 * Enum representing the types of property events in the domain.
 *
 * <p>This enumeration defines the supported event types for property lifecycle changes, such as creation and update.
 * Each enum constant is associated with a string event type identifier for use in event serialization and processing.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public enum PropertyEventType {
  PROPERTY_CREATED("property.created"),
  PROPERTY_UPDATED("property.updated");
  private final String eventType;

  /**
   * Constructs a PropertyEventType with the given event type string.
   *
   * @param eventType the string identifier for the event type
   * @since 2025-09-26
   */
  PropertyEventType(String eventType) {
    this.eventType = eventType;
  }

  /**
   * Returns the string identifier for this event type.
   *
   * @return the event type string
   * @since 2025-09-26
   */
  public String getEventType() {
    return eventType;
  }
}
