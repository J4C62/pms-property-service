package com.github.j4c62.pms.property.infrastructure.adapter.driver.advice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(GrpcExceptionAdvice.class)
class GrpcExceptionAdviceTest {
  @Autowired private GrpcExceptionAdvice advice;

  @Test
  void givenIllegalArgumentExceptionWhenHandledThenReturnsInvalidArgumentStatus() {
    var ex = new IllegalArgumentException("Invalid input");
    var status = advice.handleInvalidArgument(ex);

    assertThat(status.getCause())
        .as("Expected cause of StatusRuntimeException to be the original IllegalArgumentException")
        .isEqualTo(ex);
  }

  @Test
  void givenIllegalStateExceptionWhenHandledThenReturnsInvalidArgumentStatus() {
    var ex = new IllegalStateException("Illegal state");
    var status = advice.handleInvalidArgument(ex);

    assertThat(status.getCause())
        .as("Expected cause of StatusRuntimeException to be the original IllegalStateException")
        .isEqualTo(ex);
  }

  @Test
  void givenNullPointerExceptionWhenHandledThenReturnsInternalStatus() {
    var ex = new Exception("Null found");
    var status = advice.handleException(ex);

    assertThat(status.getCause())
        .as("Expected cause of InternalRuntimeException to be the original NullPointerException")
        .isEqualTo(ex);
  }
}
