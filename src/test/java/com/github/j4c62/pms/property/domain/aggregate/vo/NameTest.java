package com.github.j4c62.pms.property.domain.aggregate.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class NameTest {

  @Test
  void givenPropertyNameNullWhenCreatePropertyNameThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Name.of(null))
        .as("Should throw IllegalArgumentException when create an invalid name")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("ame cannot be null or blank. Please provide a valid value for Name.");
  }
}
