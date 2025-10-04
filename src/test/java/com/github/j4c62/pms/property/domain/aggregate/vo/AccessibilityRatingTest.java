package com.github.j4c62.pms.property.domain.aggregate.vo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AccessibilityRatingTest {

  @Test
  void
      givenPropertyAccessibilityScoreNegativeWhenCreateAccessibilityRatingThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> AccessibilityRating.of(-1))
        .as("The accessibility score must be between 0 and 5")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("The accessibility score must be between 0 and 5");
  }

  @Test
  void
      givenPropertyAccessibilityScore6WhenCreateAccessibilityRatingThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> AccessibilityRating.of(6))
        .as("The accessibility score must be between 0 and 5")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("The accessibility score must be between 0 and 5");
  }
}
