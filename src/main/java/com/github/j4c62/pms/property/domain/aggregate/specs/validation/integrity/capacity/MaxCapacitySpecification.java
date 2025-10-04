package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.capacity;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;

public class MaxCapacitySpecification implements DataIntegritySpecification<Integer[]> {
  @Override
  public String errorMessage(Integer[] candidate) {
    return "Maximum capacity must be at least 1";
  }

  @Override
  public boolean isSatisfiedBy(Integer[] candidate) {
    return candidate[1] > 0;
  }

  public static DataIntegritySpecification<Integer[]> create() {
    return new MaxCapacitySpecification();
  }
}
