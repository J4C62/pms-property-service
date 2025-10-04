package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.capacity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MaxCapacitySpecificationTest {
  @Test
  void givenNotSatisfiedWhenMinCapacityThrowIllegalArgumentException() {
    assertThatThrownBy(() -> MaxCapacitySpecification.create().validate(new Integer[] {1, -1}))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Maximum capacity must be at least 1");
  }
}
