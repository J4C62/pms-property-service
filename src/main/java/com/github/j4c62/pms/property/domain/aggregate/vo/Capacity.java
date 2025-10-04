package com.github.j4c62.pms.property.domain.aggregate.vo;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.capacity.MaxCapacitySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.capacity.MinCapacitySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.capacity.MinLessThanOrEqualMaxSpecification;

/**
 * Value object representing the capacity of a property.
 *
 * <p>This record encapsulates the minimum and maximum capacity for a property and ensures their validity
 * using a data integrity specification. Use {@link #of(int, int)} to create validated instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Capacity(int min, int max) {
  /**
   * Constructs a Capacity and validates the min and max values.
   *
   * @param min the minimum capacity
   * @param max the maximum capacity
   * @throws IllegalArgumentException if the values do not meet the specification
   * @since 2025-09-26
   */
  public Capacity {
    capacitySpec().validate(new Integer[] {min, max});
  }

  /**
   * Factory method to create a validated Capacity.
   *
   * @param min the minimum capacity
   * @param max the maximum capacity
   * @return a validated Capacity instance
   * @throws IllegalArgumentException if the values do not meet the specification
   * @since 2025-09-26
   */
  public static Capacity of(int min, int max) {
    return new Capacity(min, max);
  }

  /**
   * Returns the data integrity specification for the capacity.
   *
   * @return the data integrity specification
   * @since 2025-09-26
   */
  private static DataIntegritySpecification<Integer[]> capacitySpec() {
    DataIntegritySpecification<Integer[]> minCapacity = MinCapacitySpecification.create();
    DataIntegritySpecification<Integer[]> maxCapacity = MaxCapacitySpecification.create();
    DataIntegritySpecification<Integer[]> minLessThanOrEqualMaxCapacity =
        MinLessThanOrEqualMaxSpecification.create();

    return minCapacity.and(maxCapacity).and(minLessThanOrEqualMaxCapacity);
  }
}
