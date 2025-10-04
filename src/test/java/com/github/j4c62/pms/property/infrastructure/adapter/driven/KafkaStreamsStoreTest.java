package com.github.j4c62.pms.property.infrastructure.adapter.driven;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({KafkaFixture.class, KafkaStreamsStoreProperty.class})
class KafkaStreamsStoreTest {
  @Autowired KafkaStreamsStoreProperty kafkaStreamsStore;
  @MockitoBean KafkaStreams kafkaStreams;
  @MockitoBean ReadOnlyKeyValueStore<Id, PropertyEvents> keyValueStore;
  @MockitoBean InteractiveQueryService queryService;

  @SuppressWarnings("DataFlowIssue")
  @Test
  void givenNullIdWhenWhenGetEventsForPropertyThenThrowNullPointerException() {

    assertThatThrownBy(() -> kafkaStreamsStore.getEventsForProperty(null))
        .as("Expected NullPointerException")
        .isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void givenValidIdWhenGetEventsForPropertyThenReturnsPropertyEvents(
      @Autowired Id id, @Autowired PropertyEvents expectedEvents) {

    when(queryService.getQueryableStore(anyString(), any())).thenReturn(keyValueStore);

    when(kafkaStreams.store(any())).thenReturn(keyValueStore);
    when(keyValueStore.get(id)).thenReturn(expectedEvents);

    var result = kafkaStreamsStore.getEventsForProperty(id);

    assertThat(result)
        .as("Expected to retrieve property events from Kafka store for property ID: %s", id)
        .isEqualTo(expectedEvents);
    verify(keyValueStore).get(id);
  }

  @Test
  void givenNoKafkaStreamsInstanceWhenGetEventsForBookingThenThrowsNullPointerException(
      @Autowired Id id) {

    when(queryService.getQueryableStore(anyString(), any())).thenReturn(null);

    assertThatThrownBy(() -> kafkaStreamsStore.getEventsForProperty(id))
        .as("Expected NullPointerException when Kafka Streams instance is unavailable")
        .isInstanceOf(NullPointerException.class);
  }
}
