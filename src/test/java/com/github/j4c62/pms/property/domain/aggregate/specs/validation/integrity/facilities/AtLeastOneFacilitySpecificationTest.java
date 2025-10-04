package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity.facilities;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AtLeastOneFacilitySpecificationTest {
  @Test
  void givenEmptyFacilityWhenCheckThrowIllegalArgumentException() {
    assertThatThrownBy(() -> AtLeastOneFacilitySpecification.create().validate(Set.of()))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("A property must have at least one facility");
  }
}
