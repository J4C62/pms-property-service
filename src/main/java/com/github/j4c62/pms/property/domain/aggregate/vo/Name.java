package com.github.j4c62.pms.property.domain.aggregate.vo;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.NotBlankSpec;

/**
 * Value object representing the name of a property.
 *
 * <p>This record encapsulates a string value for the property name and ensures its validity using a data integrity
 * specification. Use {@link #of(String)} to create validated instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Name(String value) {

  /**
   * Constructs a Name and validates the value.
   *
   * @param value the property name to validate and store
   * @throws IllegalArgumentException if the value does not meet the specification
   * @since 2025-09-26
   */
  public Name {
    nameSpecification().validate(value);
  }

  /**
   * Factory method to create a validated Name.
   *
   * @param value the property name
   * @return a validated Name instance
   * @throws IllegalArgumentException if the value does not meet the specification
   * @since 2025-09-26
   */
  public static Name of(String value) {
    return new Name(value);
  }

  /**
   * Returns the data integrity specification for the property name.
   *
   * @return the data integrity specification
   * @since 2025-09-26
   */
  private static DataIntegritySpecification<String> nameSpecification() {
    return NotBlankSpec.from("Name");
  }
}
