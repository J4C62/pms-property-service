package com.github.j4c62.pms.property.domain.aggregate.vo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Set;
import org.junit.jupiter.api.Test;

class FacilitiesTest {

  @Test
  void givenPropertyFacilityEmptyWhenCreateFacilitiesThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Facilities.of(Set.of()))
        .as("A property must have at least one facility")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("A property must have at least one facility");
  }
}
