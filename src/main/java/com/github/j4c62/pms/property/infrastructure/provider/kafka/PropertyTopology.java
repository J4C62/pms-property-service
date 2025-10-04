package com.github.j4c62.pms.property.infrastructure.provider.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.infrastructure.provider.kafka.serde.PropertyEventSerde;
import com.github.j4c62.pms.property.infrastructure.provider.kafka.serde.PropertyEventsSerde;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

/**
 * Provides the Kafka Streams topology and Serde beans for property event processing.
 *
 * <p>This configuration class sets up the stream processing pipeline for property-related events,
 * including aggregation, state store materialization, and Serde beans for event serialization.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Configuration
@Slf4j
public class PropertyTopology {
  /** The name of the Kafka state store for property events. */
  @Value("${application.property.kafka.store-name}")
  String storeName;

  /**
   * Configures the Kafka Streams function for processing property events.
   *
   * @param propertyEventSerde Serde for PropertyEvent objects.
   * @param propertyEventsSerde Serde for PropertyEvents objects.
   * @return A function that processes a stream of property events and outputs aggregated property events.
   * @since 2025-09-26
   */
  @Bean
  public Function<KStream<String, PropertyEvent>, KStream<Id, PropertyEvents>>
      processPropertyEvents(
          Serde<PropertyEvent> propertyEventSerde, Serde<PropertyEvents> propertyEventsSerde) {

    return input ->
        input
            .map((key, cloudEvent) -> KeyValue.pair(cloudEvent.id(), cloudEvent))
            .groupByKey(Grouped.with(new JsonSerde<>(Id.class), propertyEventSerde))
            .aggregate(
                PropertyEvents::empty,
                (key, newEvent, aggregate) -> aggregate.append(newEvent),
                Materialized.<Id, PropertyEvents, KeyValueStore<Bytes, byte[]>>as(storeName)
                    .withKeySerde(new JsonSerde<>(Id.class))
                    .withValueSerde(propertyEventsSerde))
            .toStream();
  }

  /**
   * Provides a Serde bean for PropertyEvent objects.
   *
   * @param objectMapper the Jackson ObjectMapper
   * @return a Serde for PropertyEvent
   * @since 2025-09-26
   */
  @Bean
  public Serde<PropertyEvent> propertyEventSerde(ObjectMapper objectMapper) {
    return new PropertyEventSerde(objectMapper);
  }

  /**
   * Provides a Serde bean for PropertyEvents collections.
   *
   * @return a Serde for PropertyEvents
   * @since 2025-09-26
   */
  @Bean
  public Serde<PropertyEvents> propertyEventsSerde() {
    return new PropertyEventsSerde();
  }
}
