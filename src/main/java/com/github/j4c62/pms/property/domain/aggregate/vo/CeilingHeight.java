package com.github.j4c62.pms.property.domain.aggregate.vo;

/**
 * Value object representing the ceiling height of a property.
 *
 * <p>This record encapsulates the ceiling height in meters. Use {@link #of(double)} to create instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record CeilingHeight(double height) {
  /**
   * Factory method to create a CeilingHeight value object.
   *
   * @param ceilingHeight the ceiling height in meters
   * @return a new CeilingHeight instance
   * @since 2025-09-26
   */
  public static CeilingHeight of(double ceilingHeight) {
    return new CeilingHeight(ceilingHeight);
  }
}
