package com.github.j4c62.pms.property.domain.aggregate.vo;

import java.util.Currency;

/**
 * Value object representing the price of a property.
 *
 * <p>This record encapsulates the amount and currency for a property's price. Use {@link #of(double, Currency)}
 * to create instances.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Price(double amount, Currency currency) {
  /**
   * Factory method to create a Price value object.
   *
   * @param value the price amount
   * @param currency the currency
   * @return a new Price instance
   * @since 2025-09-26
   */
  public static Price of(double value, Currency currency) {
    return new Price(value, currency);
  }
}
