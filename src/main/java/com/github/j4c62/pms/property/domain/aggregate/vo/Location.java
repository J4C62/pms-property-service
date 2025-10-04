package com.github.j4c62.pms.property.domain.aggregate.vo;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.NotBlankSpec;

/**
 * Value object representing the location of a property.
 *
 * <p>This record encapsulates the location name and type, and ensures the name is valid using a
 * data integrity specification. Use {@link #of(String, LocationType)} to create validated
 * instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Location(String name, LocationType type) {
  /**
   * Constructs a Location and validates the name.
   *
   * @param name the location name to validate and store
   * @param type the location type
   * @throws IllegalArgumentException if the name does not meet the specification
   * @since 2025-09-26
   */
  public Location {
    nameSpecification().validate(name);
  }

  /**
   * Factory method to create a validated Location.
   *
   * @param name the location name
   * @param type the location type
   * @return a validated Location instance
   * @throws IllegalArgumentException if the name does not meet the specification
   * @since 2025-09-26
   */
  public static Location of(String name, LocationType type) {
    return new Location(name, type);
  }

  /**
   * Returns the data integrity specification for the location name.
   *
   * @return the data integrity specification
   * @since 2025-09-26
   */
  private static DataIntegritySpecification<String> nameSpecification() {
    return NotBlankSpec.from("Location Name");
  }
}
