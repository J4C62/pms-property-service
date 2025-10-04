package com.github.j4c62.pms.property.domain.aggregate.vo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SizeTest {

  @Test
  void givenSizeMeterLowerThan8metersThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Size.of(1.0))
        .as("Should throw IllegalArgumentException when create an invalid size")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Minimum invalid area for property");
  }
}
