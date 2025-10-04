package com.github.j4c62.pms.property.infrastructure.provider.kafka.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import java.util.ArrayList;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Serde implementation for serializing and deserializing PropertyEvents collections for Kafka.
 *
 * <p>This class uses Jackson for JSON serialization and deserialization of lists of PropertyEvent objects.
 * It leverages {@link PropertyEventSerde} for individual event (de)serialization and supports Java time types.
 *
 * <p>Intended for use in Kafka Streams and Kafka producers/consumers that handle property event collections.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public class PropertyEventsSerde implements Serde<PropertyEvents> {
  /**
   * Serde for individual PropertyEvent objects.
   */
  private final PropertyEventSerde propertyEventSerde;
  /**
   * Jackson ObjectMapper used for (de)serialization, with JavaTimeModule registered.
   */
  private final ObjectMapper objectMapper;

  /**
   * Constructs a PropertyEventsSerde with a configured ObjectMapper and PropertyEventSerde.
   *
   * @since 2025-09-26
   */
  public PropertyEventsSerde() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.propertyEventSerde = new PropertyEventSerde(objectMapper);
  }

  /**
   * Returns a serializer for PropertyEvents collections.
   *
   * @return a Kafka Serializer for PropertyEvents
   * @since 2025-09-26
   */
  @Override
  public Serializer<PropertyEvents> serializer() {
    return (topic, data) -> {
      try {
        return objectMapper.writeValueAsBytes(data.events());
      } catch (Exception e) {
        throw new SerializationException("Error serializing PropertyEvents", e);
      }
    };
  }

  /**
   * Returns a deserializer for PropertyEvents collections.
   *
   * @return a Kafka Deserializer for PropertyEvents
   * @since 2025-09-26
   */
  @Override
  public Deserializer<PropertyEvents> deserializer() {
    return (topic, bytes) -> {
      try {
        var events = new ArrayList<PropertyEvent>();
        var root = objectMapper.readTree(bytes);
        for (var node : root) {
          var event =
              propertyEventSerde.deserializer().deserialize(topic, node.toString().getBytes());
          events.add(event);
        }
        return new PropertyEvents(events);
      } catch (Exception e) {
        throw new SerializationException("Error deserializing PropertyEvents", e);
      }
    };
  }
}
