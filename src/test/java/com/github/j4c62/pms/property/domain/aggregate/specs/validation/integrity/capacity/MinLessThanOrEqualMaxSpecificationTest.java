package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.capacity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MinLessThanOrEqualMaxSpecificationTest {
  @Test
  void givenNotSatisfiedWhenMinLessThanOrEqualMaxThrowIllegalArgumentException() {
    assertThatThrownBy(
            () -> MinLessThanOrEqualMaxSpecification.create().validate(new Integer[] {9, 0}))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Minimum capacity cannot be greater than maximum capacity");
  }
}
