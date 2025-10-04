package com.github.j4c62.pms.property.domain.aggregate.event;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEventType;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import java.time.Instant;

public record PropertyUpdatedEvent(
    Id id, Data data, Status status, PropertyEventType eventType, Instant occurredAt)
    implements PropertyEvent {
  @Override
  public Result<PropertyAggregate, String> applyTo(PropertyAggregate aggregate) {
    return aggregate.updateProperty(data, status);
  }
}
