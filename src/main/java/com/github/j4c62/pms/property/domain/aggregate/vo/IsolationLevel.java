package com.github.j4c62.pms.property.domain.aggregate.vo;

/**
 * Enum representing the isolation levels available for a property.
 *
 * <p>This enumeration defines the possible privacy and independence levels for a property, such as shared,
 * semi-independent, and independent. It is used to describe the degree of separation from other units or tenants.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public enum IsolationLevel {
  SHARED,
  SEMI_INDEPENDENT,
  INDEPENDENT
}
