package com.github.j4c62.pms.property.application.acceptance.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;

import com.github.j4c62.pms.property.application.ApplicationFixture;
import com.github.j4c62.pms.property.application.acceptance.create.stage.GivenUserWantsToAddProperty;
import com.github.j4c62.pms.property.application.acceptance.create.stage.ThenTheSystemStoresThePropertyAndNotifiesTheUser;
import com.github.j4c62.pms.property.application.acceptance.create.stage.WhenTheUserAddProperty;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.junit5.SpringScenarioTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(ApplicationFixture.class)
class PropertyCreationScenarioTest
    extends SpringScenarioTest<
        GivenUserWantsToAddProperty,
        WhenTheUserAddProperty,
        ThenTheSystemStoresThePropertyAndNotifiesTheUser> {

  @ProvidedScenarioState @Autowired ApplicationFixture.SetUpFixture setUpFixture;
  @ProvidedScenarioState @Captor ArgumentCaptor<PropertyEvent> propertyEventArgumentCaptor;

  @BeforeEach
  void resetMocks() {
    reset(setUpFixture.propertyEventPublisher());
  }

  @Test
  void userCanCreatePropertySuccessfully() {
    given().theUserProvidesValidPropertyDetails();
    when().thePropertyIsCreated();
    then().thePropertyIsSavedAndUserIsNotified();
    assertThat(setUpFixture).isNotNull();
  }
}
