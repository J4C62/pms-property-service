package com.github.j4c62.pms.property.application.acceptance.update.stage;

import static com.github.j4c62.pms.property.application.PropertyTestUtils.when;

import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;

@JGivenStage
public class WhenTheUserUpdatesTheProperty {
  @ExpectedScenarioState Command propertyCommand;
  @ExpectedScenarioState PropertyHandler propertyCommandHandler;
  @ProvidedScenarioState PropertyOutput propertyOutput;

  public void thePropertyIsUpdated() {
    propertyOutput = when(propertyCommandHandler, propertyCommand);
  }
}
