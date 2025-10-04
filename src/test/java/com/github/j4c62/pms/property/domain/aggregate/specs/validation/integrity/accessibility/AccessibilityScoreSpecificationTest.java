package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.accessibility;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AccessibilityScoreSpecificationTest {
  @Test
  void givenNotSatisfiedWhenAccessibilityScoreThrowIllegalArgumentException() {
    assertThatThrownBy(() -> AccessibilityScoreSpecification.create().validate(10))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("The accessibility score must be between 0 and 5");
  }
}
