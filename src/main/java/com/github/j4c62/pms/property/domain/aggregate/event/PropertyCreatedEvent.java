package com.github.j4c62.pms.property.domain.aggregate.event;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory.createPropertyAggregate;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEventType;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import java.time.Instant;
import java.util.List;

public record PropertyCreatedEvent(
    Id id, Data data, Status status, PropertyEventType eventType, Instant occurredAt)
    implements PropertyEvent {
  @Override
  public Result<PropertyAggregate, String> applyTo(PropertyAggregate propertyAggregate) {
    return createPropertyAggregate(id, data, status, PropertyEvents.of(List.of(this)));
  }
}
