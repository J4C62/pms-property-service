package com.github.j4c62.pms.property.infrastructure.adapter.driven;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyCreatedEvent;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyUpdatedEvent;
import com.github.j4c62.pms.property.domain.driven.PropertyEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({KafkaFixture.class, KafkaProducerAdapter.class})
class KafkaProducerAdapterTest {
  @MockitoBean StreamBridge streamBridge;
  @Autowired PropertyEventPublisher propertyEventPublisher;
  @Autowired private KafkaFixture.SetUpFixtureIntegration setUpFixtureIntegration;
  @Captor private ArgumentCaptor<PropertyEvent> recordCaptor;

  @Test
  void givenNullEventWhenPublishThenThrowNullPointerException() {

    assertThatThrownBy(() -> propertyEventPublisher.publish(null))
        .as("Expected NullPointerException")
        .isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void givenCreatedEventWhenPublishThenEventIsPublished() {
    var propertyEvent = setUpFixtureIntegration.propertyCreatedEvent();

    propertyEventPublisher.publish(propertyEvent);

    thenEventIsPublished(propertyEvent, PropertyCreatedEvent.class);
  }

  @Test
  void givenUpdatedEventWhenPublishThenEventIsPublished() {
    var propertyEvent = setUpFixtureIntegration.propertyUpdatedEvent();

    propertyEventPublisher.publish(propertyEvent);
    thenEventIsPublished(propertyEvent, PropertyUpdatedEvent.class);
  }

  private <T extends PropertyEvent> void thenEventIsPublished(
      PropertyEvent bookingEvent, Class<T> eventClass) {
    verify(streamBridge).send(anyString(), recordCaptor.capture());
    var resultValue = recordCaptor.getValue();
    assertThat(resultValue)
        .as("Expected event to be equal to the published propertyEvent")
        .isEqualTo(bookingEvent);

    assertThat(resultValue)
        .as("Expected event to be exactly instance of %s", eventClass.getSimpleName())
        .isExactlyInstanceOf(eventClass);
  }
}
