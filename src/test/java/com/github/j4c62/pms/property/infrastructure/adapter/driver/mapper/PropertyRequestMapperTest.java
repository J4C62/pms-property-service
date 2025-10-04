package com.github.j4c62.pms.property.infrastructure.adapter.driver.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(PropertyRequestMapperImpl.class)
class PropertyRequestMapperTest {
  @Autowired private PropertyRequestMapper propertyRequestMapper;

  @Test
  void givenEmptyPropertyIdWhenMappingPropertyIdThenThrowIllegalArgumentException() {
    thenThrowIllegalArgumentException(() -> propertyRequestMapper.mapId(""));
  }

  private void thenThrowIllegalArgumentException(
      ThrowableAssert.ThrowingCallable throwingCallable) {
    assertThatThrownBy(throwingCallable)
        .as("Expected IllegalArgumentException")
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }
}
