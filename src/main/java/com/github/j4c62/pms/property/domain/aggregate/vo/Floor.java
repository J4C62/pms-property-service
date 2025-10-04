package com.github.j4c62.pms.property.domain.aggregate.vo;

/**
 * Value object representing the floor level of a property.
 *
 * <p>This record encapsulates the floor number (level) for a property. Use {@link #of(int)} to create instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Floor(int level) {

  /**
   * Factory method to create a Floor value object.
   *
   * @param level the floor level
   * @return a new Floor instance
   * @since 2025-09-26
   */
  public static Floor of(int level) {
    return new Floor(level);
  }
}
