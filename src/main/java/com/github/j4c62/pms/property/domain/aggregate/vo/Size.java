package com.github.j4c62.pms.property.domain.aggregate.vo;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.size.MinInvalidAreaSpecification;

/**
 * Value object representing the size (area in square meters) of a property.
 *
 * <p>This record encapsulates the area in meters and ensures its validity using a data integrity
 * specification. Use {@link #of(double)} to create validated instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Size(double meters) {
  /**
   * Constructs a Size and validates the area in meters.
   *
   * @param meters the area in square meters to validate and store
   * @throws IllegalArgumentException if the value does not meet the specification
   * @since 2025-09-26
   */
  public Size {
    sizeSpec().validate(meters);
  }

  /**
   * Factory method to create a validated Size.
   *
   * @param meters the area in square meters
   * @return a validated Size instance
   * @throws IllegalArgumentException if the value does not meet the specification
   * @since 2025-09-26
   */
  public static Size of(double meters) {
    return new Size(meters);
  }

  /**
   * Returns the data integrity specification for the size.
   *
   * @return the data integrity specification
   * @since 2025-09-26
   */
  private static DataIntegritySpecification<Double> sizeSpec() {
    return MinInvalidAreaSpecification.create();
  }
}
