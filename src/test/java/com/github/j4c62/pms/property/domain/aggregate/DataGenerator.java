package com.github.j4c62.pms.property.domain.aggregate;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory.createPropertyData;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Facility.JACUZZI;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Facility.OFFICE_SPACE;
import static com.github.j4c62.pms.property.domain.aggregate.vo.IsolationLevel.INDEPENDENT;
import static com.github.j4c62.pms.property.domain.aggregate.vo.LayoutType.CLOSED;
import static com.github.j4c62.pms.property.domain.aggregate.vo.LocationType.URBAN;
import static com.github.j4c62.pms.property.domain.aggregate.vo.Type.SUITE;

import com.github.j4c62.pms.property.domain.aggregate.vo.Data;

import java.util.Currency;
import java.util.Set;

public class DataGenerator {

  protected static Data getPropertyData(double price) {
    return createPropertyData(
            "Luxury Suite",
            SUITE,
            1,
            2,
            40,
            2.5,
            CLOSED,
            INDEPENDENT,
            true,
            3,
            1,
            3,
            5,
            Set.of(OFFICE_SPACE, JACUZZI),
            "Madrid",
            URBAN,
            price,
            Currency.getInstance("EUR"))
        .unwrap();
  }
}
