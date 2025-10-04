package com.github.j4c62.pms.property.application.acceptance.create.stage;

import static com.github.j4c62.pms.property.application.PropertyTestUtils.when;

import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;

@JGivenStage
public class WhenTheUserAddProperty {

  @ExpectedScenarioState Command propertyCommand;
  @ExpectedScenarioState PropertyHandler propertyCommandHandler;
  @ProvidedScenarioState PropertyOutput propertyOutput;

  public void thePropertyIsCreated() {
    propertyOutput = when(propertyCommandHandler, propertyCommand);
  }
}
