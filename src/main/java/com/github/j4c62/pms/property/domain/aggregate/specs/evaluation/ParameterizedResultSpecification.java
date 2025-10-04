package com.github.j4c62.pms.property.domain.aggregate.specs.evaluation;

import com.github.j4c62.pms.property.domain.aggregate.specs.predicates.ParameterizedSpecification;

@FunctionalInterface
public interface ParameterizedResultSpecification<T, P> extends ParameterizedSpecification<T, P> {
  Result<Void, String> evaluate(T candidate, P parameter);

  default boolean isSatisfiedBy(T candidate, P parameter) {
    return evaluate(candidate, parameter).isOk();
  }

  default ResultSpecification<T> withParameter(P parameter) {
    return candidate -> evaluate(candidate, parameter);
  }

  default ParameterizedResultSpecification<T, P> and(ParameterizedResultSpecification<T, P> other) {
    return (candidate, param) -> {
      Result<Void, String> left = this.evaluate(candidate, param);
      if (left.isErr()) {
        return left;
      }

      Result<Void, String> right = other.evaluate(candidate, param);
      if (right.isErr()) {
        return right;
      }

      return Result.ok(null);
    };
  }

  default ParameterizedResultSpecification<T, P> or(ParameterizedResultSpecification<T, P> other) {
    return (candidate, param) -> {
      Result<Void, String> left = this.evaluate(candidate, param);
      if (left.isOk()) {
        return left;
      }

      Result<Void, String> right = other.evaluate(candidate, param);
      if (right.isOk()) {
        return right;
      }

      return Result.err(
          "Both conditions failed:\n- " + left.unwrapErr() + "\n- " + right.unwrapErr());
    };
  }

  default ParameterizedResultSpecification<T, P> not() {
    return (candidate, param) -> {
      Result<Void, String> result = this.evaluate(candidate, param);
      return result.isOk() ? Result.err("Negated condition was satisfied") : Result.ok(null);
    };
  }
}
