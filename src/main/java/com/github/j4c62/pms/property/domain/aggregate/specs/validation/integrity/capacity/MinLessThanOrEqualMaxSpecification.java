package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.capacity;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;

public class MinLessThanOrEqualMaxSpecification implements DataIntegritySpecification<Integer[]> {
  @Override
  public String errorMessage(Integer[] candidate) {
    return "Minimum capacity cannot be greater than maximum capacity";
  }

  @Override
  public boolean isSatisfiedBy(Integer[] candidate) {
    return candidate[0] <= candidate[1];
  }

  public static DataIntegritySpecification<Integer[]> create() {
    return new MinLessThanOrEqualMaxSpecification();
  }
}
