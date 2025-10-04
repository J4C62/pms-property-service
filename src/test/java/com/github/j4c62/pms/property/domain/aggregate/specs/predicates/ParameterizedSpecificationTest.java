package com.github.j4c62.pms.property.domain.aggregate.specs.predicates;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ParameterizedSpecificationTest {

  private final ParameterizedSpecification<String, Integer> alwaysTrue = (candidate, param) -> true;

  private final ParameterizedSpecification<String, Integer> alwaysFalse =
      (candidate, param) -> false;

  @Test
  void givenTrueAndTrueWhenApplyAndThenReturnTrue() {
    var spec = alwaysTrue.and(alwaysTrue);
    assertThat(spec.isSatisfiedBy("test", 1)).isTrue();
  }

  @Test
  void givenTrueAndFalseWhenApplyAndThenReturnFalse() {
    var spec = alwaysTrue.and(alwaysFalse);
    assertThat(spec.isSatisfiedBy("test", 1)).isFalse();
  }

  @Test
  void givenFalseAndTrueWhenApplyAndThenReturnFalse() {
    var spec = alwaysFalse.and(alwaysTrue);
    assertThat(spec.isSatisfiedBy("test", 1)).isFalse();
  }

  @Test
  void givenFalseAndFalseWhenApplyAndThenReturnFalse() {
    var spec = alwaysFalse.and(alwaysFalse);
    assertThat(spec.isSatisfiedBy("test", 1)).isFalse();
  }

  @Test
  void givenTrueOrTrueWhenApplyOrThenReturnTrue() {
    var spec = alwaysTrue.or(alwaysTrue);
    assertThat(spec.isSatisfiedBy("test", 1)).isTrue();
  }

  @Test
  void givenTrueOrFalseWhenApplyOrThenReturnTrue() {
    var spec = alwaysTrue.or(alwaysFalse);
    assertThat(spec.isSatisfiedBy("test", 1)).isTrue();
  }

  @Test
  void givenFalseOrTrueWhenApplyOrThenReturnTrue() {
    var spec = alwaysFalse.or(alwaysTrue);
    assertThat(spec.isSatisfiedBy("test", 1)).isTrue();
  }

  @Test
  void givenFalseOrFalseWhenApplyOrThenReturnFalse() {
    var spec = alwaysFalse.or(alwaysFalse);
    assertThat(spec.isSatisfiedBy("test", 1)).isFalse();
  }

  @Test
  void givenTrueWhenApplyNotThenReturnFalse() {
    var spec = alwaysTrue.not();
    assertThat(spec.isSatisfiedBy("test", 1)).isFalse();
  }

  @Test
  void givenFalseWhenApplyNotThenReturnTrue() {
    var spec = alwaysFalse.not();
    assertThat(spec.isSatisfiedBy("test", 1)).isTrue();
  }

  @Test
  void givenWithParameterWhenApplyThenDelegateToParameterizedSpec() {
    ParameterizedSpecification<String, Integer> lengthEquals = (str, len) -> str.length() == len;

    var spec = lengthEquals.withParameter(4);
    assertThat(spec.isSatisfiedBy("test")).isTrue();
    assertThat(spec.isSatisfiedBy("nope")).isTrue();
  }
}
