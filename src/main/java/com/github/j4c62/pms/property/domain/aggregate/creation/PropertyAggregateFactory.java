package com.github.j4c62.pms.property.domain.aggregate.creation;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.vo.AccessibilityRating;
import com.github.j4c62.pms.property.domain.aggregate.vo.Capacity;
import com.github.j4c62.pms.property.domain.aggregate.vo.CeilingHeight;
import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Facilities;
import com.github.j4c62.pms.property.domain.aggregate.vo.Facility;
import com.github.j4c62.pms.property.domain.aggregate.vo.Floor;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.IsolationLevel;
import com.github.j4c62.pms.property.domain.aggregate.vo.LayoutType;
import com.github.j4c62.pms.property.domain.aggregate.vo.Location;
import com.github.j4c62.pms.property.domain.aggregate.vo.LocationType;
import com.github.j4c62.pms.property.domain.aggregate.vo.Name;
import com.github.j4c62.pms.property.domain.aggregate.vo.Price;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.aggregate.vo.Size;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import com.github.j4c62.pms.property.domain.aggregate.vo.Type;
import com.github.j4c62.pms.property.domain.aggregate.vo.Window;
import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.command.types.CreatePropertyCommand;
import java.util.Currency;
import java.util.Set;
import java.util.UUID;

public class PropertyAggregateFactory {

  public static Result<PropertyAggregate, String> createPropertyAggregate(
      Id id, Data data, Status status, PropertyEvents propertyEvents) {
    return Result.ok(new PropertyAggregate(id, data, status, propertyEvents));
  }

  public static Result<PropertyAggregate, String> createPropertyAggregate(Command command) {
    var createPropertyCommand = (CreatePropertyCommand) command;
    return Result.ok(
        new PropertyAggregate(
            Id.of(UUID.randomUUID()),
            createPropertyCommand.data(),
            createPropertyCommand.status(),
            null));
  }

  public static Result<Data, String> createPropertyData(
      String name,
      Type type,
      int minCapacity,
      int maxCapacity,
      double size,
      double ceilingHeight,
      LayoutType layout,
      IsolationLevel isolation,
      boolean privateEntrance,
      int windows,
      int closedRooms,
      int floor,
      int accessibility,
      Set<Facility> facilities,
      String locationName,
      LocationType locationType,
      double price,
      Currency currency) {
    return Result.ok(
        new Data(
            Name.of(name),
            type,
            Capacity.of(minCapacity, maxCapacity),
            Size.of(size),
            CeilingHeight.of(ceilingHeight),
            layout,
            isolation,
            privateEntrance,
            Window.of(windows),
            closedRooms,
            Floor.of(floor),
            AccessibilityRating.of(accessibility),
            Facilities.of(facilities),
            Location.of(locationName, locationType),
            Price.of(price, currency)));
  }
}
