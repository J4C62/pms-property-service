package com.github.j4c62.pms.property.application.acceptance.create.stage;

import com.github.j4c62.pms.property.application.ApplicationFixture;
import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;

@JGivenStage
public class GivenUserWantsToAddProperty {
  @ExpectedScenarioState ApplicationFixture.SetUpFixture setUpFixture;
  @ProvidedScenarioState Command propertyCommand;
  @ProvidedScenarioState PropertyHandler propertyCommandHandler;

  public void theUserProvidesValidPropertyDetails() {
    propertyCommand = setUpFixture.createPropertyCommand();
    propertyCommandHandler = setUpFixture.propertyCommandHandler();
  }
}
