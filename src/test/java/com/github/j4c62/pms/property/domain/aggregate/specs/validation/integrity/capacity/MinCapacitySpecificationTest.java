package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.capacity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MinCapacitySpecificationTest {
  @Test
  void givenNotSatisfiedWhenMinCapacityThrowIllegalArgumentException() {
    assertThatThrownBy(() -> MinCapacitySpecification.create().validate(new Integer[] {-1, 0}))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Minimum capacity must be at least 1");
  }
}
