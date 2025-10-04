package com.github.j4c62.pms.property.domain.aggregate.specs.evaluation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParameterizedResultSpecificationTest {
  private final ParameterizedResultSpecification<String, Integer> alwaysOk =
      (candidate, param) -> Result.ok(null);
  private final ParameterizedResultSpecification<String, Integer> alwaysErr =
      (candidate, param) -> Result.err("alwaysErr failed");
  private final ParameterizedResultSpecification<String, Integer> lengthEquals =
      (candidate, param) ->
          candidate.length() == param ? Result.ok(null) : Result.err("Length mismatch");
  private final ParameterizedResultSpecification<String, Integer> startsWithF =
      (candidate, param) ->
          candidate.startsWith("f") ? Result.ok(null) : Result.err("Does not start with f");

  @Test
  void givenValidCandidateWhenEvaluateThenReturnsOk() {
    var result = alwaysOk.evaluate("test", 1);
    assertThat(result.isOk()).isTrue();
  }

  @Test
  void givenInvalidCandidateWhenEvaluateThenReturnsErr() {
    var result = alwaysErr.evaluate("test", 1);
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).isEqualTo("alwaysErr failed");
  }

  @Test
  void givenBothOkInAndWhenEvaluateThenReturnsOk() {
    var andSpec = alwaysOk.and(alwaysOk);
    var result = andSpec.evaluate("any", 1);
    assertThat(result).isEqualTo(Result.ok(null));
  }

  @Test
  void givenFirstErrInAndWhenEvaluateThenReturnsFirstErr() {
    var andSpec = alwaysErr.and(alwaysOk);
    var result = andSpec.evaluate("any", 1);
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).isEqualTo("alwaysErr failed");
  }

  @Test
  void givenSecondErrInAndWhenEvaluateThenReturnsSecondErr() {
    var andSpec = alwaysOk.and(alwaysErr);
    var result = andSpec.evaluate("any", 1);
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).isEqualTo("alwaysErr failed");
  }

  @Test
  void givenFirstOkInOrWhenEvaluateThenReturnsOk() {
    var orSpec = alwaysOk.or(alwaysErr);
    var result = orSpec.evaluate("value", 1);
    assertThat(result).isEqualTo(Result.ok(null));
  }

  @Test
  void givenOkInNotWhenEvaluateThenReturnsErr() {
    var notSpec = alwaysOk.not();
    var result = notSpec.evaluate("value", 1);
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).isEqualTo("Negated condition was satisfied");
  }

  @Test
  void givenErrInNotWhenEvaluateThenReturnsOk() {
    var notSpec = alwaysErr.not();
    var result = notSpec.evaluate("value", 1);
    assertThat(result).isEqualTo(Result.ok(null));
  }

  @Test
  void givenSatisfiedSpecWhenIsSatisfiedByThenReturnsTrue() {
    assertThat(lengthEquals.isSatisfiedBy("foo", 3)).isTrue();
  }

  @Test
  void givenUnsatisfiedSpecWhenIsSatisfiedByThenReturnsFalse() {
    assertThat(lengthEquals.isSatisfiedBy("foo", 2)).isFalse();
  }

  @Test
  void
      givenParameterWhenWithParameterThenReturnsResultSpecificationThatEvaluatesWithGivenParameter() {
    var spec = lengthEquals.withParameter(3);
    assertThat(spec.evaluate("foo").isOk()).isTrue();
    assertThat(spec.evaluate("fo").isErr()).isTrue();
  }

  @Test
  void givenTwoSpecsWhenOrThenReturnsOkIfEitherSpecSatisfied() {

    var orSpec = lengthEquals.or(startsWithF);

    assertThat(orSpec.evaluate("foo", 3).isOk()).isTrue();
    assertThat(orSpec.evaluate("far", 2).isOk()).isTrue();
    assertThat(orSpec.evaluate("bar", 3).isOk()).isTrue();

    var result = orSpec.evaluate("bar", 2);

    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).contains("Both conditions failed");
  }
}
