package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;

public class NotNullSpec<T> implements DataIntegritySpecification<T> {
  private final String fieldName;

  public NotNullSpec(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public String errorMessage(T candidate) {
    return String.format(
        "%s cannot be null. Please provide a valid value for %s.", fieldName, fieldName);
  }

  @Override
  public boolean isSatisfiedBy(T candidate) {
    return candidate != null;
  }

  public static <T> DataIntegritySpecification<T> from(String fieldName) {
    return new NotNullSpec<>(fieldName);
  }
}
