package com.github.j4c62.pms.property.domain.aggregate.vo;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.facilities.AtLeastOneFacilitySpecification;
import java.util.Set;

/**
 * Value object representing a set of facilities for a property.
 *
 * <p>This record encapsulates a set of {@link Facility} items and ensures at least one facility is
 * present using a data integrity specification. Use {@link #of(Set)} to create validated instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Facilities(Set<Facility> items) {
  /**
   * Constructs a Facilities record and validates the set of facilities.
   *
   * @param items the set of facilities to validate and store
   * @throws IllegalArgumentException if the set does not meet the specification
   * @since 2025-09-26
   */
  public Facilities {
    facilitySpecification().validate(items);
  }

  /**
   * Factory method to create a validated Facilities record.
   *
   * @param facilities the set of facilities
   * @return a validated Facilities instance
   * @throws IllegalArgumentException if the set does not meet the specification
   * @since 2025-09-26
   */
  public static Facilities of(Set<Facility> facilities) {
    return new Facilities(facilities);
  }

  /**
   * Returns the data integrity specification for the facilities set.
   *
   * @return the data integrity specification
   * @since 2025-09-26
   */
  private static DataIntegritySpecification<Set<Facility>> facilitySpecification() {
    return AtLeastOneFacilitySpecification.create();
  }
}
