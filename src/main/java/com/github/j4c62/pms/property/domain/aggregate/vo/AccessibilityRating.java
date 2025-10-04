package com.github.j4c62.pms.property.domain.aggregate.vo;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.accessibility.AccessibilityScoreSpecification;

/**
 * Value object representing the accessibility rating of a property.
 *
 * <p>This record encapsulates an integer score and ensures its validity using a data integrity
 * specification. Use {@link #of(int)} to create validated instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record AccessibilityRating(int score) {
  /**
   * Constructs an AccessibilityRating and validates the score.
   *
   * @param score the accessibility score to validate and store
   * @throws IllegalArgumentException if the score does not meet the specification
   * @since 2025-09-26
   */
  public AccessibilityRating {
    scoreSpecification().validate(score);
  }

  /**
   * Factory method to create a validated AccessibilityRating.
   *
   * @param score the accessibility score
   * @return a validated AccessibilityRating instance
   * @throws IllegalArgumentException if the score does not meet the specification
   * @since 2025-09-26
   */
  public static AccessibilityRating of(int score) {
    return new AccessibilityRating(score);
  }

  /**
   * Returns the data integrity specification for the accessibility score.
   *
   * @return the data integrity specification
   * @since 2025-09-26
   */
  private static DataIntegritySpecification<Integer> scoreSpecification() {
    return AccessibilityScoreSpecification.create();
  }
}
