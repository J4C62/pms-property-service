package com.github.j4c62.pms.property.domain.aggregate;

import static com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate.restoreFrom;
import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory.createPropertyAggregate;
import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createPropertyEvent;
import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createUpdatePropertyEvent;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.ACTIVE;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.DELETED;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

class PropertyAggregateTest extends DataGenerator {
  private static PropertyEvents getPropertyEvents(PropertyEvent... propertyEvent) {
    return PropertyEvents.of(List.of(propertyEvent));
  }

  private PropertyAggregate getPropertyAggregate(Status status, PropertyEvents propertyEvents) {
    var propertyId = Id.of(UUID.randomUUID());
    var data = getPropertyData(250.0);
    return createPropertyAggregate(propertyId, data, status, propertyEvents).unwrap();
  }

  private PropertyEvent getPropertyEvent() {
    return createPropertyEvent(getPropertyAggregate(ACTIVE, PropertyEvents.empty()));
  }

  private PropertyAggregate getDefaultPropertyAggregate(Status status) {
    return getPropertyAggregate(status, PropertyEvents.empty());
  }

  @Test
  void givenEmptyPropertyEventsWhenRestorePropertyFromEventsThenThrowIllegalArgumentException() {
    var propertyEvents = PropertyEvents.empty();
    thenMethodThrowsIllegalArgumentException(
        () -> restoreFrom(propertyEvents), "Cannot restore aggregate from empty event list");
  }

  @Test
  void givenTheFirstOneIsNotPropertyCreatedEventWhenRestorePropertyThenThrowsIllegalException() {
    var propertyId = Id.of(UUID.randomUUID());
    var updatedEvent = createUpdatePropertyEvent(propertyId, null, null);
    var propertyEvents = getPropertyEvents(updatedEvent);
    thenMethodThrowsIllegalArgumentException(
        () -> restoreFrom(propertyEvents), "First event must be PropertyCreatedEvent");
  }

  @Test
  void givenPropertyEventValidWhenRestorePropertyThenReturnPropertyAggregate() {
    var propertyId = getPropertyEvent().id();

    var propertyEvents =
        getPropertyEvents(
            getPropertyEvent(), createUpdatePropertyEvent(propertyId, null, INACTIVE));

    var propertyAggregate = restoreFrom(propertyEvents);

    assertThat(propertyAggregate.unwrap().propertyEvents().events())
        .as("Property events should not be empty after restoring from valid events")
        .isNotEmpty();
  }

  @Test
  void givenDeletedPropertyWhenUpdatePropertyThenThrowIllegalStateException() {
    var propertyAggregate = getDefaultPropertyAggregate(DELETED);
    thenResultIsErrWithMessage(
        () -> propertyAggregate.updateProperty(getPropertyData(150.0), DELETED), "is deleted");
  }

  @Test
  void givenValidPropertyWhenDeletePropertyThenStatusChangeToDeleted() {
    var propertyAggregate = getDefaultPropertyAggregate(ACTIVE);
    var deletedProperty = propertyAggregate.updateProperty(getPropertyData(150.0), DELETED);
    assertThat(deletedProperty.unwrap().status())
        .as("Property status should be Deleted after deletion")
        .isEqualTo(DELETED);
  }

  @Test
  void givenValidPropertyWhenInactivePropertyThenStatusChangeToInactive() {
    var propertyAggregate = getDefaultPropertyAggregate(ACTIVE);
    var inactivatedProperty = propertyAggregate.updateProperty(getPropertyData(150.0), INACTIVE);
    assertThat(inactivatedProperty.unwrap().status())
        .as("Property status should be inactive after inactivation")
        .isEqualTo(INACTIVE);
  }

  @Test
  void givenInactivePropertyWhenInactivePropertyThenThrowIllegalStateException() {
    var propertyAggregate = getDefaultPropertyAggregate(INACTIVE);
    thenResultIsErrWithMessage(
        () -> propertyAggregate.updateProperty(getPropertyData(150.0), INACTIVE),
        "Property already has status: INACTIVE");
  }

  @Test
  void givenNullPropertyEventsWhenCreatePropertyThenValueOfPropertyEventsIsEmpty() {
    var propertyAggregate = getPropertyAggregate(ACTIVE, null);
    assertThat(propertyAggregate.propertyEvents().events())
        .as("Property events list should be empty when no events are provided")
        .isEmpty();
  }

  @Test
  void givenNoNullPropertyEventsWhenCreatePropertyThenValueOfPropertyEventsIsNotEmpty() {
    var propertyEvent = getPropertyEvent();
    var propertyEvents = getPropertyEvents(propertyEvent);

    var propertyAggregate = getPropertyAggregate(ACTIVE, propertyEvents);

    assertThat(propertyAggregate.propertyEvents().events())
        .element(0)
        .as("The first event should be the same as the provided property event")
        .isEqualTo(propertyEvent);
  }

  private void thenResultIsErrWithMessage(
      Supplier<Result<?, String>> result, String expectedMessage) {
    assertThat(result.get().unwrapErr())
        .as("Error message should contain expected text")
        .contains(expectedMessage);
  }

  private void thenMethodThrowsIllegalArgumentException(
      ThrowableAssert.ThrowingCallable method, String expectedMessage) {
    assertThatThrownBy(method)
        .as("Should throw IllegalArgumentException when restoring from empty event list")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains(expectedMessage);
  }
}
