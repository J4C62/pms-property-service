package com.github.j4c62.pms.property.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEventType;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import com.github.j4c62.pms.property.domain.driven.PropertyEventPublisher;
import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;
import org.mockito.ArgumentCaptor;

public class PropertyTestUtils {

  public static PropertyOutput when(PropertyHandler propertyCommandHandler, Command command) {
    return propertyCommandHandler.handle(command);
  }

  public static void thenTheEventIsPublished(
      PropertyEventPublisher propertyEventPublisher,
      int times,
      ArgumentCaptor<PropertyEvent> argumentCaptor,
      PropertyEventType propertyEventType,
      PropertyOutput propertyOutput) {
    await()
        .untilAsserted(
            () -> verify(propertyEventPublisher, times(times)).publish(argumentCaptor.capture()));
    var propertyCreatedEvent = argumentCaptor.getValue();
    assertThat(propertyCreatedEvent.eventType())
        .as("Expected event type to be %s", propertyEventType)
        .isEqualTo(propertyEventType);
    assertThat(propertyCreatedEvent.id())
        .as("Expected property ID to match output property ID")
        .isEqualTo(propertyOutput.id());
  }

  public static void thenPropertyOutputValid(
      PropertyOutput output, Status expectedStatus, String desc) {
    assertThat(output).as("PropertyOutput should not be null").isNotNull();

    assertThat(output.id()).as("PropertyOutput.bookingId should not be null").isNotNull();

    assertThat(output.status()).as(desc).isEqualTo(expectedStatus);
  }
}
