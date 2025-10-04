package com.github.j4c62.pms.property.domain.driver.command.types;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createPropertyEvent;
import static java.util.Objects.requireNonNull;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import com.github.j4c62.pms.property.domain.driver.command.Command;

public record CreatePropertyCommand(Data data, Status status) implements Command {
  @Override
  public Result<PropertyAggregate, String> applyTo(PropertyAggregate aggregate) {
    return createPropertyEvent(requireNonNull(aggregate)).applyTo(null);
  }
}
