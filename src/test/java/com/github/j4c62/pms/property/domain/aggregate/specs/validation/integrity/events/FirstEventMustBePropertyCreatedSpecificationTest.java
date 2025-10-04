package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.events;

import com.github.j4c62.pms.property.domain.aggregate.DataGenerator;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createUpdatePropertyEvent;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FirstEventMustBePropertyCreatedSpecificationTest extends DataGenerator {
  @Test
  void givenFirstEventUpdateWhenCheckThrowIllegalArgumentException() {
    assertThatThrownBy(
            () ->
                FirstEventMustBePropertyCreatedSpecification.create()
                    .validate(
                        PropertyEvents.of(
                            List.of(
                                createUpdatePropertyEvent(
                                    Id.of(UUID.randomUUID()),
                                    getPropertyData(2.0),
                                    Status.INACTIVE)))))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("irst event must be PropertyCreatedEvent");
  }
}
