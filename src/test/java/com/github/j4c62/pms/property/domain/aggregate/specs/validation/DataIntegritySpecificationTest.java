package com.github.j4c62.pms.property.domain.aggregate.specs.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DataIntegritySpecificationTest {
  private final DataIntegritySpecification<String> alwaysValid = candidate -> true;

  private final DataIntegritySpecification<String> alwaysInvalid = candidate -> false;

  @Test
  void givenValidCandidateWhenValidateThenDoesNotThrow() {
    assertThatCode(() -> alwaysValid.validate("test")).doesNotThrowAnyException();
  }

  @Test
  void givenInvalidCandidateWhenValidateThenThrowsIllegalArgumentException() {
    assertThatThrownBy(() -> alwaysInvalid.validate("test"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Data integrity validation failed");
  }

  @Test
  void givenFirstFailsInAndWhenValidateThenThrowsFirstErrorMessage() {
    DataIntegritySpecification<String> spec1 =
        new DataIntegritySpecification<>() {
          @Override
          public boolean isSatisfiedBy(String candidate) {
            return false;
          }

          @Override
          public String errorMessage(String candidate) {
            return "First spec failed";
          }
        };

    DataIntegritySpecification<String> spec2 = candidate -> true;
    DataIntegritySpecification<String> combined = spec1.and(spec2);

    assertThatThrownBy(() -> combined.validate("input"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("First spec failed");
  }

  @Test
  void givenSecondFailsInAndWhenValidateThenThrowsSecondErrorMessage() {
    DataIntegritySpecification<String> spec1 = candidate -> true;
    DataIntegritySpecification<String> spec2 =
        new DataIntegritySpecification<>() {
          @Override
          public boolean isSatisfiedBy(String candidate) {
            return false;
          }

          @Override
          public String errorMessage(String candidate) {
            return "Second spec failed";
          }
        };

    DataIntegritySpecification<String> combined = spec1.and(spec2);

    assertThatThrownBy(() -> combined.validate("input"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Second spec failed");
  }

  @Test
  void givenBothValidInAndWhenValidateThenDoesNotThrow() {
    DataIntegritySpecification<String> combined = alwaysValid.and(alwaysValid);

    assertThatCode(() -> combined.validate("test")).doesNotThrowAnyException();
  }

  @Test
  void givenAtLeastOneValidInOrWhenValidateThenDoesNotThrow() {
    DataIntegritySpecification<String> spec = alwaysValid.or(alwaysInvalid);

    assertThatCode(() -> spec.validate("input")).doesNotThrowAnyException();
  }

  @Test
  void givenBothInvalidInOrWhenValidateThenThrowsCombinedMessage() {
    DataIntegritySpecification<String> spec = alwaysInvalid.or(alwaysInvalid);

    assertThatThrownBy(() -> spec.validate("test"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Data integrity validation failed OR Data integrity validation failed");
  }

  @Test
  void givenInvalidInNotWhenValidateThenDoesNotThrow() {
    DataIntegritySpecification<String> spec = alwaysInvalid.not();

    assertThatCode(() -> spec.validate("anything")).doesNotThrowAnyException();
  }

  @Test
  void givenValidInNotWhenValidateThenThrowsNegatedConditionMessage() {
    DataIntegritySpecification<String> spec = alwaysValid.not();

    assertThatThrownBy(() -> spec.validate("hello"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Negated condition was satisfied");
  }
}
