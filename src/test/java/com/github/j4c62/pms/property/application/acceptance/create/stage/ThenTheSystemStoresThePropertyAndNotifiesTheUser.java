package com.github.j4c62.pms.property.application.acceptance.create.stage;

import static com.github.j4c62.pms.property.application.PropertyTestUtils.thenPropertyOutputValid;
import static com.github.j4c62.pms.property.application.PropertyTestUtils.thenTheEventIsPublished;
import static com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEventType.PROPERTY_CREATED;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.ACTIVE;

import com.github.j4c62.pms.property.application.ApplicationFixture;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.mockito.ArgumentCaptor;

@JGivenStage
public class ThenTheSystemStoresThePropertyAndNotifiesTheUser {

  @ExpectedScenarioState PropertyOutput propertyOutput;
  @ExpectedScenarioState ArgumentCaptor<PropertyEvent> propertyEventArgumentCaptor;
  @ExpectedScenarioState ApplicationFixture.SetUpFixture setUpFixture;

  public void thePropertyIsSavedAndUserIsNotified() {
    thenTheEventIsPublished(
        setUpFixture.propertyEventPublisher(),
        1,
        propertyEventArgumentCaptor,
        PROPERTY_CREATED,
        propertyOutput);
    System.out.println(propertyOutput);
    thenPropertyOutputValid(propertyOutput, ACTIVE, "status is Active");
  }
}
