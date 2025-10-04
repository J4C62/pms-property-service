package com.github.j4c62.pms.property.domain.aggregate.vo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CapacityTest {

  @Test
  void givenPropertyMinCapacity0WhenCreatePropertyCapacityThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Capacity.of(0, 1))
        .as("Should throw IllegalArgumentException when create an invalid capacity")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Minimum capacity must be at least 1");
  }

  @Test
  void givenPropertyMaxCapacity0WhenCreatePropertyCapacityThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Capacity.of(1, 0))
        .as("Should throw IllegalArgumentException when create an invalid capacity")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Maximum capacity must be at least 1");
  }

  @Test
  void
      givenPropertyMinCapacityGreaterThanMaxCapacityWhenCreatePropertyCapacityThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Capacity.of(2, 1))
        .as("Should throw IllegalArgumentException when create an invalid capacity")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Minimum capacity cannot be greater than maximum capacity");
  }
}
