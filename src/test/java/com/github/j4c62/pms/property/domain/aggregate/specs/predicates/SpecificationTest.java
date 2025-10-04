package com.github.j4c62.pms.property.domain.aggregate.specs.predicates;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecificationTest {

  private final Specification<String> alwaysTrue = candidate -> true;
  private final Specification<String> alwaysFalse = candidate -> false;

  @Test
  void givenTrueAndTrueWhenApplyAndThenReturnTrue() {
    Specification<String> spec = alwaysTrue.and(alwaysTrue);
    assertThat(spec.isSatisfiedBy("test")).isTrue();
  }

  @Test
  void givenTrueAndFalseWhenApplyAndThenReturnFalse() {
    Specification<String> spec = alwaysTrue.and(alwaysFalse);
    assertThat(spec.isSatisfiedBy("test")).isFalse();
  }

  @Test
  void givenFalseAndTrueWhenApplyAndThenReturnFalse() {
    Specification<String> spec = alwaysFalse.and(alwaysTrue);
    assertThat(spec.isSatisfiedBy("test")).isFalse();
  }

  @Test
  void givenFalseAndFalseWhenApplyAndThenReturnFalse() {
    Specification<String> spec = alwaysFalse.and(alwaysFalse);
    assertThat(spec.isSatisfiedBy("test")).isFalse();
  }

  @Test
  void givenTrueOrTrueWhenApplyOrThenReturnTrue() {
    Specification<String> spec = alwaysTrue.or(alwaysTrue);
    assertThat(spec.isSatisfiedBy("test")).isTrue();
  }

  @Test
  void givenTrueOrFalseWhenApplyOrThenReturnTrue() {
    Specification<String> spec = alwaysTrue.or(alwaysFalse);
    assertThat(spec.isSatisfiedBy("test")).isTrue();
  }

  @Test
  void givenFalseOrTrueWhenApplyOrThenReturnTrue() {
    Specification<String> spec = alwaysFalse.or(alwaysTrue);
    assertThat(spec.isSatisfiedBy("test")).isTrue();
  }

  @Test
  void givenFalseOrFalseWhenApplyOrThenReturnFalse() {
    Specification<String> spec = alwaysFalse.or(alwaysFalse);
    assertThat(spec.isSatisfiedBy("test")).isFalse();
  }

  @Test
  void givenTrueWhenApplyNotThenReturnFalse() {
    Specification<String> spec = alwaysTrue.not();
    assertThat(spec.isSatisfiedBy("test")).isFalse();
  }

  @Test
  void givenFalseWhenApplyNotThenReturnTrue() {
    Specification<String> spec = alwaysFalse.not();
    assertThat(spec.isSatisfiedBy("test")).isTrue();
  }
}
