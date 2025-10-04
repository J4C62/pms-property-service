package com.github.j4c62.pms.property.domain.aggregate.creation;

import static com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEventType.PROPERTY_CREATED;
import static com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEventType.PROPERTY_UPDATED;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyCreatedEvent;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyUpdatedEvent;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import java.time.Instant;

public final class PropertyEventFactory {

  private PropertyEventFactory() {}

  public static PropertyEvent createUpdatePropertyEvent(Id id, Data data, Status status) {
    return new PropertyUpdatedEvent(id, data, status, PROPERTY_UPDATED, Instant.now());
  }

  public static PropertyEvent createPropertyEvent(PropertyAggregate aggregate) {
    return new PropertyCreatedEvent(
        aggregate.id(), aggregate.data(), aggregate.status(), PROPERTY_CREATED, Instant.now());
  }

  public static PropertyEvent createPropertyEvent(Id id, Data data, Status status) {
    return new PropertyCreatedEvent(id, data, status, PROPERTY_CREATED, Instant.now());
  }
}
