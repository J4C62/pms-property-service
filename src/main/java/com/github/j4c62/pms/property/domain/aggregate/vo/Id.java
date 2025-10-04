package com.github.j4c62.pms.property.domain.aggregate.vo;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.NotNullSpec;
import java.util.UUID;

/**
 * Value object representing the unique identifier of a property.
 *
 * <p>This record encapsulates a UUID value and ensures its validity using a data integrity specification.
 * Use {@link #of(UUID)} to create validated instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Id(UUID value) {
  /**
   * Constructs an Id and validates the UUID value.
   *
   * @param value the UUID to validate and store
   * @throws IllegalArgumentException if the value does not meet the specification
   * @since 2025-09-26
   */
  public Id {
    idSpecification().validate(value);
  }

  /**
   * Factory method to create a validated Id.
   *
   * @param value the UUID value
   * @return a validated Id instance
   * @throws IllegalArgumentException if the value does not meet the specification
   * @since 2025-09-26
   */
  public static Id of(UUID value) {
    return new Id(value);
  }

  /**
   * Returns the data integrity specification for the Id.
   *
   * @return the data integrity specification
   * @since 2025-09-26
   */
  private static DataIntegritySpecification<UUID> idSpecification() {
    return NotNullSpec.from("Id cannot be null");
  }
}
