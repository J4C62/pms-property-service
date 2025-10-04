package com.github.j4c62.pms.property.domain.driver.command.types;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createUpdatePropertyEvent;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import com.github.j4c62.pms.property.domain.driver.command.Command;

public record UpdatePropertyCommand(Id id, Data data, Status status) implements Command {
  @Override
  public Result<PropertyAggregate, String> applyTo(PropertyAggregate aggregate) {
    var event = createUpdatePropertyEvent(id, data, status);
    return event.applyTo(aggregate);
  }
}
