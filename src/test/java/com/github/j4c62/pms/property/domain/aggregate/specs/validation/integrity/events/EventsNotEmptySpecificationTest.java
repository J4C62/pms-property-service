package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.events;

import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EventsNotEmptySpecificationTest {
  @Test
  void givenEmptyEventsWhenCheckThrowIllegalArgumentException() {
    assertThatThrownBy(() -> EventsNotEmptySpecification.create().validate(PropertyEvents.empty()))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Cannot restore aggregate from empty event list");
  }
}
