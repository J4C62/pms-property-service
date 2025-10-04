package com.github.j4c62.pms.property.infrastructure.adapter.driven;

import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.driven.PropertyEventStore;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Component;

/**
 * Kafka Streams adapter for querying property event state stores.
 *
 * <p>This component provides access to the Kafka Streams state store for property events, allowing
 * retrieval of event histories for a given property ID using interactive queries.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaStreamsStoreProperty implements PropertyEventStore {

  private final InteractiveQueryService queryService;

  @Value("${application.property.kafka.store-name}")
  private String storeName;

  /**
   * Retrieves the event history for a property from the Kafka Streams state store.
   *
   * @param id the property ID
   * @return the property events for the given ID, or null if not found
   * @since 2025-09-26
   */
  @Override
  @NonNull
  public PropertyEvents getEventsForProperty(@NonNull Id id) {

    ReadOnlyKeyValueStore<Id, PropertyEvents> store =
        queryService.getQueryableStore(storeName, QueryableStoreTypes.keyValueStore());

    return store.get(id);
  }
}
