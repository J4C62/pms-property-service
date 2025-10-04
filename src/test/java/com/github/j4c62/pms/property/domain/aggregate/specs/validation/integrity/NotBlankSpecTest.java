package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class NotBlankSpecTest {
  @Test
  void givenNotSatisfiedWhenNotBlankThrowIllegalArgumentException() {
    assertThatThrownBy(() -> NotBlankSpec.from("Field").validate(""))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Field cannot be null or blank. Please provide a valid value for Field.");
  }
}
