package com.github.j4c62.pms.property.domain.aggregate;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory.createPropertyAggregate;
import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createUpdatePropertyEvent;

import com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyCreatedEvent;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.ResultSpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.state.StateNotDeletedSpec;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.state.StateStatusMustChangeSpec;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.events.EventsNotEmptySpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.events.FirstEventMustBePropertyCreatedSpecification;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import java.util.List;

/**
 * Aggregate root representing a property in the domain model.
 *
 * <p>This record encapsulates the state and event history of a property, including its ID, data,
 * status, and associated property events. It provides methods for restoring the aggregate from
 * events and for domain logic related to property state transitions.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record PropertyAggregate(Id id, Data data, Status status, PropertyEvents propertyEvents) {

  public PropertyAggregate {
    propertyEvents = propertyEvents == null ? PropertyEvents.empty() : propertyEvents;
  }

  public static Result<PropertyAggregate, String> restoreFrom(PropertyEvents events) {

    return Result.<Void, String>ok(null)
        .peek(ignore -> eventsSpecification().validate(events))
        .flatMap(
            ignore -> {
              PropertyCreatedEvent created = (PropertyCreatedEvent) events.events().getFirst();

              return PropertyAggregateFactory.createPropertyAggregate(
                      created.id(),
                      created.data(),
                      created.status(),
                      PropertyEvents.of(List.of(created)))
                  .flatMap(
                      base -> {
                        var tailEvents =
                            PropertyEvents.of(events.events().subList(1, events.events().size()));
                        return tailEvents.replayOn(base);
                      });
            });
  }

  public Result<PropertyAggregate, String> updateProperty(Data data, Status newStatus) {
    return updatePropertySpec(newStatus)
        .evaluate(this)
        .flatMap(
            ignore -> withEvent(createUpdatePropertyEvent(id, data, newStatus), data, newStatus));
  }

  private Result<PropertyAggregate, String> withEvent(
      PropertyEvent event, Data newData, Status newStatus) {
    return createPropertyAggregate(id, newData, newStatus, PropertyEvents.of(List.of(event)));
  }

  private static DataIntegritySpecification<PropertyEvents> eventsSpecification() {
    DataIntegritySpecification<PropertyEvents> firstEventMustBePropertyCreatedEvent =
        FirstEventMustBePropertyCreatedSpecification.create();
    DataIntegritySpecification<PropertyEvents> eventsNotEmpty =
        EventsNotEmptySpecification.create();
    return eventsNotEmpty.and(firstEventMustBePropertyCreatedEvent);
  }

  private static ResultSpecification<PropertyAggregate> updatePropertySpec(Status newStatus) {
    return StateNotDeletedSpec.create().and(StateStatusMustChangeSpec.create(newStatus));
  }
}
