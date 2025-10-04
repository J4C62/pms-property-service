package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.size;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MinInvalidAreaSpecificationTest {

  @Test
  void givenInvalidAreaWhenCheckThrowIllegalArgumentException() {
    assertThatThrownBy(() -> MinInvalidAreaSpecification.create().validate(2.0))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Minimum invalid area for property");
  }
}
