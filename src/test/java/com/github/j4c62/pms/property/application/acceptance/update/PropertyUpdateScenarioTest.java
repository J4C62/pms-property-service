package com.github.j4c62.pms.property.application.acceptance.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;

import com.github.j4c62.pms.property.application.ApplicationFixture;
import com.github.j4c62.pms.property.application.acceptance.update.stage.GivenUserWantsToModifyProperty;
import com.github.j4c62.pms.property.application.acceptance.update.stage.ThenTheSystemStoresTheUpdatedPropertyAndNotifiesTheUser;
import com.github.j4c62.pms.property.application.acceptance.update.stage.WhenTheUserUpdatesTheProperty;
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
class PropertyUpdateScenarioTest
    extends SpringScenarioTest<
        GivenUserWantsToModifyProperty,
        WhenTheUserUpdatesTheProperty,
        ThenTheSystemStoresTheUpdatedPropertyAndNotifiesTheUser> {
  @ProvidedScenarioState @Autowired ApplicationFixture.SetUpFixture setUpFixture;
  @ProvidedScenarioState @Captor ArgumentCaptor<PropertyEvent> propertyEventArgumentCaptor;

  @BeforeEach
  void resetMocks() {
    reset(setUpFixture.propertyEventPublisher());
  }

  @Test
  void userCanUpdatePropertyToInactiveSuccessfully() {
    given().theUserProvidesValidPropertyAndPropertyExists();
    when().thePropertyIsUpdated();
    then().thePropertyChangesAreSavedAndTheUserIsNotified();
    assertThat(setUpFixture).isNotNull();
  }
}
