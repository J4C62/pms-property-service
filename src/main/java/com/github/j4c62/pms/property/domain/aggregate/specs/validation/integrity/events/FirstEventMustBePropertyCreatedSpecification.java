package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.events;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyCreatedEvent;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;

public class FirstEventMustBePropertyCreatedSpecification
    implements DataIntegritySpecification<PropertyEvents> {
  @Override
  public String errorMessage(PropertyEvents candidate) {
    return "First event must be PropertyCreatedEvent";
  }

  @Override
  public boolean isSatisfiedBy(PropertyEvents candidate) {
    return candidate.events().getFirst() instanceof PropertyCreatedEvent;
  }

  public static DataIntegritySpecification<PropertyEvents> create() {
    return new FirstEventMustBePropertyCreatedSpecification();
  }
}
