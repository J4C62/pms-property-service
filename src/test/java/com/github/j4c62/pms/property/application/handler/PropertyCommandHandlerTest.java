package com.github.j4c62.pms.property.application.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.github.j4c62.pms.property.application.ApplicationFixture;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(ApplicationFixture.class)
class PropertyCommandHandlerTest {

  @Test
  void givenNullCommandWhenHandleThenReturnNullPointerException(
      @Autowired PropertyHandler propertyCommandHandler) {
    assertThatThrownBy(() -> propertyCommandHandler.handle(null))
        .as("Expected NullPointerException when handling a null command")
        .isInstanceOf(NullPointerException.class)
        .message()
        .contains("command is marked non-null but is null");
  }

  @Test
  void givenUnsupportedCommandWhenHandleThenReturnIllegalArgumentException(
      @Autowired PropertyHandler propertyCommandHandler) {
    assertThatThrownBy(() -> propertyCommandHandler.handle(Result::ok))
        .as("Expected IllegalArgumentException when handling a invalid command type")
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .contains("Unsupported command type");
  }

  @Test
  void givenUpdateCommandWithSameStatusWhenHandleThenNoPublishEventAndReturnOutputWithErrors(
      @Autowired ApplicationFixture.SetUpFixture setUpFixture) {

    var failingCommand = setUpFixture.failingCommand();

    var output = setUpFixture.propertyCommandHandler().handle(failingCommand);

    verifyNoMoreInteractions(setUpFixture.propertyEventPublisher());
    assertThat(output.errors()).contains("Property already has status: ACTIVE");
  }
}
