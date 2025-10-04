package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.accessibility;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;

/**
 * Data integrity specification for validating accessibility scores.
 *
 * <p>This specification ensures that the accessibility score is within the allowed range (0 to 5).
 * Use {@link #create()} to obtain an instance.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public class AccessibilityScoreSpecification implements DataIntegritySpecification<Integer> {
  /**
   * Returns the error message if the candidate score is invalid.
   *
   * @param candidate the score to validate
   * @return the error message for invalid scores
   * @since 2025-09-26
   */
  @Override
  public String errorMessage(Integer candidate) {
    return "The accessibility score must be between 0 and 5";
  }

  /**
   * Checks if the candidate score is within the valid range (0 to 5).
   *
   * @param candidate the score to check
   * @return true if the score is valid, false otherwise
   * @since 2025-09-26
   */
  @Override
  public boolean isSatisfiedBy(Integer candidate) {
    return candidate >= 0 && candidate <= 5;
  }

  /**
   * Factory method to create a new AccessibilityScoreSpecification instance.
   *
   * @return a new data integrity specification for accessibility scores
   * @since 2025-09-26
   */
  public static DataIntegritySpecification<Integer> create() {
    return new AccessibilityScoreSpecification();
  }
}
