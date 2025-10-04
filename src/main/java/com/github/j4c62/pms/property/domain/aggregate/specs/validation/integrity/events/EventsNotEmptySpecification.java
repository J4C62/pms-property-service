package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.events;

import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;

public class EventsNotEmptySpecification implements DataIntegritySpecification<PropertyEvents> {

  @Override
  public String errorMessage(PropertyEvents candidate) {
    return "Cannot restore aggregate from empty event list";
  }

  @Override
  public boolean isSatisfiedBy(PropertyEvents candidate) {
    return !candidate.events().isEmpty();
  }

  public static DataIntegritySpecification<PropertyEvents> create() {
    return new EventsNotEmptySpecification();
  }
}
