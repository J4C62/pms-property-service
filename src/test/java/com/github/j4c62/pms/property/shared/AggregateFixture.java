package com.github.j4c62.pms.property.shared;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory.createPropertyAggregate;
import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory.createPropertyData;
import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createPropertyEvent;
import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyEventFactory.createUpdatePropertyEvent;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Facility.JACUZZI;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Facility.OFFICE_SPACE;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Facility.OPEN_KITCHEN;
import static com.github.j4c62.pms.property.domain.aggregate.vo.IsolationLevel.INDEPENDENT;
import static com.github.j4c62.pms.property.domain.aggregate.vo.LayoutType.CLOSED;
import static com.github.j4c62.pms.property.domain.aggregate.vo.LocationType.RURAL;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.ACTIVE;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Status.INACTIVE;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Type.LOFT;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.domain.aggregate.specs.validation.DataIntegritySpecification;
import com.github.j4c62.pms.property.domain.aggregate.vo.AccessibilityRating;
import com.github.j4c62.pms.property.domain.aggregate.vo.Capacity;
import com.github.j4c62.pms.property.domain.aggregate.vo.CeilingHeight;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Facilities;
import com.github.j4c62.pms.property.domain.aggregate.vo.Floor;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.Name;
import com.github.j4c62.pms.property.domain.aggregate.vo.Price;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.aggregate.vo.Size;

import java.util.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AggregateFixture {

  @Bean
  public Id id() {
    return Id.of(UUID.randomUUID());
  }

  @Bean
  public Name name() {
    return new Name("Luxury Suite");
  }

  @Bean
  public Capacity capacity() {
    return Capacity.of(1, 2);
  }

  @Bean
  public CeilingHeight ceilingHeight() {
    return CeilingHeight.of(2.5);
  }

  @Bean
  public Size size() {
    return Size.of(40);
  }

  @Bean
  public Floor floor() {
    return Floor.of(3);
  }

  @Bean
  public AccessibilityRating accessibilityRating() {
    return AccessibilityRating.of(5);
  }

  @Bean
  public Facilities facilities() {
    return Facilities.of(Set.of(OFFICE_SPACE, JACUZZI, OPEN_KITCHEN));
  }

  @Bean
  public Price price() {
    return Price.of(150, Currency.getInstance("EUR"));
  }

  @Bean
  public Data data(
      Id id,
      Name name,
      Capacity capacity,
      Size size,
      CeilingHeight ceilingHeight,
      Floor floor,
      AccessibilityRating accessibilityRating,
      Facilities facilities,
      Price price) {
    return createPropertyData(
            name.value(),
            LOFT,
            capacity.min(),
            capacity.max(),
            size.meters(),
            ceilingHeight.height(),
            CLOSED,
            INDEPENDENT,
            true,
            1,
            1,
            floor.level(),
            accessibilityRating.score(),
            facilities.items(),
            "Madrid",
            RURAL,
            price.amount(),
            price.currency())
        .unwrap();
  }

  @Bean
  @Qualifier("propertyCreatedEvent")
  public PropertyEvent propertyCreatedEvent(Id id, Data data) {
    return createPropertyEvent(id, data, ACTIVE);
  }

  @Bean
  @Qualifier("propertyUpdatedEvent")
  public PropertyEvent propertyUpdatedEvent(Id id, Data data) {
    return createUpdatePropertyEvent(id, data, INACTIVE);
  }

  @Bean
  public PropertyEvents propertyEvents(
      @Qualifier("propertyCreatedEvent") PropertyEvent createdEvent) {
    return PropertyEvents.of(List.of(createdEvent));
  }

  @Bean
  public PropertyAggregate propertyAggregate(Id id, Data data, PropertyEvents propertyEvents) {
    return createPropertyAggregate(id, data, ACTIVE, propertyEvents).unwrap();
  }
}
