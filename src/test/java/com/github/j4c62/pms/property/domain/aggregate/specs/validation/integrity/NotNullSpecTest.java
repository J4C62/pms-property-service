package com.github.j4c62.pms.property.domain.aggregate.specs.validation.integrity;

import com.github.j4c62.pms.property.domain.aggregate.vo.Capacity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class NotNullSpecTest {

  @Test
  void givenNotSatisfiedCandidateWhenCheckNullThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> NotNullSpec.from("Field").validate(null))
        .as("Should throw IllegalArgumentException")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Field cannot be null. Please provide a valid value for Field.");
  }
}
