package com.github.j4c62.pms.property.domain.aggregate.specs.evaluation;

import com.github.j4c62.pms.property.domain.aggregate.specs.predicates.Specification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultSpecificationTest {
  private final ResultSpecification<String> alwaysOk = candidate -> Result.ok(null);
  private final ResultSpecification<String> alwaysErr = candidate -> Result.err("alwaysErr failed");

  private final Specification<String> specFalse = candidate -> false;
  private final Specification<String> specTrue = candidate -> true;

  @Test
  void givenValidCandidateWhenEvaluateThenReturnsOk() {
    var result = alwaysOk.evaluate("test");
    assertThat(result.isOk()).isTrue();
  }

  @Test
  void givenInvalidCandidateWhenEvaluateThenReturnsErr() {
    var result = alwaysErr.evaluate("test");
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).isEqualTo("alwaysErr failed");
  }

  @Test
  void givenBothOkInAndWhenEvaluateThenReturnsOk() {
    var andSpec = alwaysOk.and(alwaysOk);
    var result = andSpec.evaluate("any");
    assertThat(result).isEqualTo(Result.ok(null));
  }

  @Test
  void givenFirstErrInAndWhenEvaluateThenReturnsFirstErr() {
    var andSpec = alwaysErr.and(alwaysOk);
    var result = andSpec.evaluate("any");
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).isEqualTo("alwaysErr failed");
  }

  @Test
  void givenSecondErrInAndWhenEvaluateThenReturnsSecondErr() {
    var andSpec = alwaysOk.and(alwaysErr);
    var result = andSpec.evaluate("any");
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).isEqualTo("alwaysErr failed");
  }

  @Test
  void givenFallbackSpecFailsInAndWhenEvaluateThenReturnsGenericMessage() {
    var andSpec = alwaysOk.and(specFalse); // specFalse is not ResultSpecification
    var result = andSpec.evaluate("value");
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).contains("Specification");
  }

  @Test
  void givenFallbackSpecPassesInAndWhenEvaluateThenReturnsOk() {
    var andSpec = alwaysOk.and(specTrue);
    var result = andSpec.evaluate("value");
    assertThat(result).isEqualTo(Result.ok(null));
  }

  @Test
  void givenFirstOkInOrWhenEvaluateThenReturnsOk() {
    var orSpec = alwaysOk.or(alwaysErr);
    var result = orSpec.evaluate("value");
    assertThat(result).isEqualTo(Result.ok(null));
  }

  @Test
  void givenFallbackSpecTrueInOrWhenEvaluateThenReturnsOk() {
    var orSpec = alwaysErr.or(specTrue);
    var result = orSpec.evaluate("value");
    assertThat(result).isEqualTo(Result.ok(null));
  }

  @Test
  void givenBothFailInOrWhenEvaluateThenReturnsCombinedErr() {
    var orSpec = alwaysErr.or(specFalse);
    var result = orSpec.evaluate("value");
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).contains("Both conditions failed");
  }

  @Test
  void givenOkInNotWhenEvaluateThenReturnsErr() {
    var notSpec = alwaysOk.not();
    var result = notSpec.evaluate("value");
    assertThat(result.isErr()).isTrue();
    assertThat(result.unwrapErr()).isEqualTo("Negated condition was satisfied");
  }

  @Test
  void givenErrInNotWhenEvaluateThenReturnsOk() {
    var notSpec = alwaysErr.not();
    var result = notSpec.evaluate("value");
    assertThat(result).isEqualTo(Result.ok(null));
  }
}
