package com.github.j4c62.pms.property.domain.aggregate.specs.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParameterizedDataIntegritySpecificationTest {

  private final ParameterizedDataIntegritySpecification<String, Integer> alwaysValid =
      (candidate, param) -> true;

  private final ParameterizedDataIntegritySpecification<String, Integer> alwaysInvalid =
      (candidate, param) -> false;

  @Test
  void givenValidCandidateAndParameterWhenValidateThenDoesNotThrow() {
    assertThatCode(() -> alwaysValid.validate("test", 1)).doesNotThrowAnyException();
  }

  @Test
  void givenInvalidCandidateAndParameterWhenValidateThenThrowsIllegalArgumentException() {
    assertThatThrownBy(() -> alwaysInvalid.validate("test", 1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Data integrity validation failed");
  }

  @Test
  void givenFirstFailsInAndWhenValidateThenThrowsFirstErrorMessage() {
    ParameterizedDataIntegritySpecification<String, Integer> first =
        new ParameterizedDataIntegritySpecification<>() {
          @Override
          public boolean isSatisfiedBy(String candidate, Integer param) {
            return false;
          }

          @Override
          public String errorMessage(String candidate, Integer param) {
            return "First spec failed";
          }
        };

    ParameterizedDataIntegritySpecification<String, Integer> second = (c, p) -> true;

    var combined = first.and(second);

    assertThatThrownBy(() -> combined.validate("value", 1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("First spec failed");
  }

  @Test
  void givenSecondFailsInAndWhenValidateThenThrowsSecondErrorMessage() {
    ParameterizedDataIntegritySpecification<String, Integer> first = (c, p) -> true;

    ParameterizedDataIntegritySpecification<String, Integer> second =
        new ParameterizedDataIntegritySpecification<>() {
          @Override
          public boolean isSatisfiedBy(String candidate, Integer param) {
            return false;
          }

          @Override
          public String errorMessage(String candidate, Integer param) {
            return "Second spec failed";
          }
        };

    var combined = first.and(second);

    assertThatThrownBy(() -> combined.validate("value", 1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Second spec failed");
  }

  @Test
  void givenBothValidInAndWhenValidateThenDoesNotThrow() {
    var combined = alwaysValid.and(alwaysValid);

    assertThatCode(() -> combined.validate("ok", 10)).doesNotThrowAnyException();
  }

  @Test
  void givenAtLeastOneValidInOrWhenValidateThenDoesNotThrow() {
    var combined = alwaysValid.or(alwaysInvalid);

    assertThatCode(() -> combined.validate("value", 5)).doesNotThrowAnyException();
  }

  @Test
  void givenBothInvalidInOrWhenValidateThenThrowsCombinedErrorMessage() {
    var combined = alwaysInvalid.or(alwaysInvalid);

    assertThatThrownBy(() -> combined.validate("bad", 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Data integrity validation failed OR Data integrity validation failed");
  }

  @Test
  void givenInvalidInNotWhenValidateThenDoesNotThrow() {
    var notSpec = alwaysInvalid.not();

    assertThatCode(() -> notSpec.validate("value", 2)).doesNotThrowAnyException();
  }

  @Test
  void givenValidInNotWhenValidateThenThrowsNegatedConditionMessage() {
    var notSpec = alwaysValid.not();

    assertThatThrownBy(() -> notSpec.validate("any", 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Negated condition was satisfied");
  }
}
