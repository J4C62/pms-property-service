package com.github.j4c62.pms.property.infrastructure.provider.kafka.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyCreatedEvent;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Serde implementation for serializing and deserializing PropertyEvent objects for Kafka.
 *
 * <p>This class uses Jackson for JSON serialization and deserialization, and resolves the correct
 * event type based on the eventType field in the JSON payload. It supports both PropertyCreatedEvent
 * and PropertyUpdatedEvent types.
 *
 * <p>Intended for use in Kafka Streams and Kafka producers/consumers that handle property events.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@RequiredArgsConstructor
public class PropertyEventSerde implements Serde<PropertyEvent> {

  /**
   * Jackson ObjectMapper used for (de)serialization.
   */
  private final ObjectMapper mapper;

  /**
   * Resolves the Java class for a given event type string.
   *
   * @param type the event type string (e.g., "PROPERTY_CREATED")
   * @return the corresponding event class
   * @throws IllegalArgumentException if the event type is unknown
   * @since 2025-09-26
   */
  private static Class<? extends Record> resolveType(String type) {
    return switch (type) {
      case "PROPERTY_CREATED" -> PropertyCreatedEvent.class;
      case "PROPERTY_UPDATED" -> PropertyUpdatedEvent.class;
      default -> throw new IllegalArgumentException("Unknown eventType: %s".formatted(type));
    };
  }

  /**
   * Returns a serializer for PropertyEvent objects.
   *
   * @return a Kafka Serializer for PropertyEvent
   * @since 2025-09-26
   */
  @Override
  public Serializer<PropertyEvent> serializer() {
    return (topic, data) -> {
      try {
        return mapper.writeValueAsBytes(data);
      } catch (Exception e) {
        throw new SerializationException("Error serializing PropertyEvent", e);
      }
    };
  }

  /**
   * Returns a deserializer for PropertyEvent objects.
   *
   * @return a Kafka Deserializer for PropertyEvent
   * @since 2025-09-26
   */
  @Override
  public Deserializer<PropertyEvent> deserializer() {
    return (topic, bytes) -> {
      try {
        var node = mapper.readTree(bytes);
        var type = node.get("eventType").asText();
        return (PropertyEvent) mapper.treeToValue(node, resolveType(type));
      } catch (Exception e) {
        throw new SerializationException("Error deserializing PropertyEvent", e);
      }
    };
  }
}
