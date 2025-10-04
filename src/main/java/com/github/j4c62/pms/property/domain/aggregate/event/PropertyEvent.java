package com.github.j4c62.pms.property.domain.aggregate.event;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEventType;
import java.time.Instant;

public sealed interface PropertyEvent permits PropertyCreatedEvent, PropertyUpdatedEvent {

  Id id();

  Result<PropertyAggregate, String> applyTo(PropertyAggregate propertyAggregate);

  PropertyEventType eventType();

  Instant occurredAt();
}
