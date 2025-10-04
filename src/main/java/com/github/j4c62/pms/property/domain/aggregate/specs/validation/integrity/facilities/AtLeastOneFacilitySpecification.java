package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.facilities;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.vo.Facility;
import java.util.Set;

public class AtLeastOneFacilitySpecification implements DataIntegritySpecification<Set<Facility>> {
  @Override
  public String errorMessage(Set<Facility> candidate) {
    return "A property must have at least one facility";
  }

  @Override
  public boolean isSatisfiedBy(Set<Facility> candidate) {
    return !candidate.isEmpty();
  }

  public static DataIntegritySpecification<Set<Facility>> create() {
    return new AtLeastOneFacilitySpecification();
  }
}
