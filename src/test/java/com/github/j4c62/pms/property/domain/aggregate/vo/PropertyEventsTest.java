package com.github.j4c62.pms.property.domain.aggregate.vo;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createPropertyEvent;
import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createUpdatePropertyEvent;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.ACTIVE;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.j4c62.pms.property.domain.aggregate.DataGenerator;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PropertyEventsTest extends DataGenerator {

  private static PropertyEvents createPropertyEvents(List<PropertyEvent> propertyEvents) {
    return PropertyEvents.of(propertyEvents);
  }

  @Test
  void givenPropertyEventsNullWhenCreatePropertyEventsThenEventsIsEmpty() {
    var events = PropertyEvents.of(null);

    assertThat(events.events())
        .as("Expected events to be empty when initialized with null")
        .isEmpty();
  }

  @Test
  void givenPropertyEventToAppendWhenAppendEventsThenPropertyEventsNotNull() {
    var propertyEventBase =
        createPropertyEvent(Id.of(UUID.randomUUID()), getPropertyData(0), ACTIVE);
    var propertyEventToAppend =
        createUpdatePropertyEvent(Id.of(UUID.randomUUID()), getPropertyData(0), INACTIVE);
    var events = createPropertyEvents(List.of(propertyEventBase));

    var appendEvents = events.append(propertyEventToAppend);

    assertThat(appendEvents.events())
        .as("Expected events to contain both the base and appended property events")
        .contains(propertyEventBase, propertyEventToAppend);
  }

//
}
