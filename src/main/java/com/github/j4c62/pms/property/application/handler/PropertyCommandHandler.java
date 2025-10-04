package com.github.j4c62.pms.property.application.handler;

import static com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate.restoreFrom;
import static java.util.Objects.requireNonNull;

import com.github.j4c62.pms.property.application.creation.mapper.PropertyAggregateMapper;
import com.github.j4c62.pms.property.application.creation.mapper.PropertyOutputMapper;
import com.github.j4c62.pms.property.application.distpacher.PropertyEventDispatcher;
import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.driven.PropertyEventStore;
import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.command.types.CreatePropertyCommand;
import com.github.j4c62.pms.property.domain.driver.command.types.UpdatePropertyCommand;
import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyCommandHandler implements PropertyHandler {
  private final PropertyEventStore propertyEventStore;
  private final PropertyEventDispatcher propertyEventDispatcher;
  private final PropertyAggregateMapper propertyAggregateMapper;
  private final PropertyOutputMapper propertyOutputMapper;

  @Override
  public PropertyOutput handle(@NonNull Command command) {
    if (command instanceof CreatePropertyCommand createPropertyCommand) {
      var aggregate = propertyAggregateMapper.toAggregate(createPropertyCommand);
      return handleCommand(command, Result.ok(aggregate));
    } else if (command instanceof UpdatePropertyCommand updatePropertyCommand) {
      var events = propertyEventStore.getEventsForProperty(updatePropertyCommand.id());
      return handleCommand(command, restoreFrom(requireNonNull(events)));
    }

    throw new IllegalArgumentException(
        "Unsupported command type: %s".formatted(command.getClass()));
  }

  private PropertyOutput handleCommand(
      Command command, Result<PropertyAggregate, String> aggregateResult) {
    return aggregateResult
        .flatMap(command::applyTo)
        .peek(propertyEventDispatcher::dispatch)
        .map(propertyOutputMapper::toPropertyOutput)
        .fold(
            error -> {
              Id id = aggregateResult.map(PropertyAggregate::id).getOrElse(() -> null);
              return propertyOutputMapper.fromError(id, error);
            },
            output -> output);
  }
}
