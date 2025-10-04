package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;

public class NotBlankSpec implements DataIntegritySpecification<String> {
  private final String fieldName;

  public NotBlankSpec(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public String errorMessage(String candidate) {
    return String.format(
        "%s cannot be null or blank. Please provide a valid value for %s.", fieldName, fieldName);
  }

  @Override
  public boolean isSatisfiedBy(String candidate) {
    return candidate != null && !candidate.isEmpty();
  }

  public static DataIntegritySpecification<String> from(String fieldName) {
    return new NotBlankSpec(fieldName);
  }
}
