package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.size;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;

/**
 * Data integrity specification for validating the minimum area of a property.
 *
 * <p>This specification ensures that the area of a property is not less than 8.0 square meters.
 * Use {@link #create()} to obtain an instance for validation logic.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public class MinInvalidAreaSpecification implements DataIntegritySpecification<Double> {
  /**
   * Returns the error message if the candidate area is invalid.
   *
   * @param candidate the area to validate
   * @return the error message for invalid area
   * @since 2025-09-26
   */
  @Override
  public String errorMessage(Double candidate) {
    return "Minimum invalid area for property";
  }

  /**
   * Checks if the candidate area is at least 8.0 square meters.
   *
   * @param candidate the area to check
   * @return true if the area is valid, false otherwise
   * @since 2025-09-26
   */
  @Override
  public boolean isSatisfiedBy(Double candidate) {
    return candidate >= 8.0;
  }

  /**
   * Factory method to create a new MinInvalidAreaSpecification instance.
   *
   * @return a new data integrity specification for minimum area
   * @since 2025-09-26
   */
  public static DataIntegritySpecification<Double> create() {
    return new MinInvalidAreaSpecification();
  }
}
