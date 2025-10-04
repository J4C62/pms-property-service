package com.github.j4c62.pms.property.shared;

import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.ACTIVE;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.DELETED;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.INACTIVE;
import static org.mockito.Mockito.mock;

import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.Capacity;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.command.types.CreatePropertyCommand;
import com.github.j4c62.pms.property.domain.driver.command.types.UpdatePropertyCommand;
import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(AggregateFixture.class)
public class DriverFixture {

  @Bean
  @Qualifier("createPropertyCommand")
  public Command createPropertyCommand(Data data) {
    return new CreatePropertyCommand(data, ACTIVE);
  }

  @Bean
  @Qualifier("updateActivePropertyCommand")
  public Command updatePropertyCommand(Id id, Data data) {
    return new UpdatePropertyCommand(id, data, Status.INACTIVE);
  }

  @Bean
  @Qualifier("failingCommand")
  public Command failingPropertyCommand(Id id, Data data) {
    return new UpdatePropertyCommand(id, data, ACTIVE);
  }

  @Bean
  @ConditionalOnMissingBean(PropertyHandler.class)
  public PropertyHandler propertyCreator() {
    return req ->
        switch (req) {
          case CreatePropertyCommand cmd ->
              new PropertyOutput(Id.of(UUID.randomUUID()), ACTIVE, null);
          case UpdatePropertyCommand cmd -> new PropertyOutput(cmd.id(), INACTIVE, null);
          default -> new PropertyOutput(Id.of(UUID.randomUUID()), DELETED, null);
        };
  }
}
