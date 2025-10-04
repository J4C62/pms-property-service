package com.github.j4c62.pms.property.domain.aggregate.specs.evaluation;

import com.github.j4c62.pms.property.domain.aggregate.specs.predicates.Specification;

@FunctionalInterface
public interface ResultSpecification<T> extends Specification<T> {
  Result<Void, String> evaluate(T candidate);

  @Override
  default boolean isSatisfiedBy(T candidate) {
    return evaluate(candidate).isOk();
  }

  @Override
  default ResultSpecification<T> and(Specification<T> other) {
    if (other instanceof ResultSpecification<T> rs) {
      return candidate -> {
        Result<Void, String> left = this.evaluate(candidate);
        if (left.isErr()) {
          return left;
        }

        Result<Void, String> right = rs.evaluate(candidate);
        if (right.isErr()) {
          return right;
        }

        return Result.ok(null);
      };
    } else {
      return candidate -> {
        Result<Void, String> left = this.evaluate(candidate);
        if (left.isErr()) {
          return left;
        }

        boolean rightBool = other.isSatisfiedBy(candidate);
        if (!rightBool) {
          return Result.err("Specification " + other.getClass().getSimpleName() + " failed");
        }

        return Result.ok(null);
      };
    }
  }

  @Override
  default ResultSpecification<T> or(Specification<T> other) {
    return candidate -> {
      Result<Void, String> left = this.evaluate(candidate);
      if (left.isOk()) {
        return left;
      }
      boolean rightBool = other.isSatisfiedBy(candidate);
      if (rightBool) {
        return Result.ok(null);
      }
      return Result.err("Both conditions failed, including other Specification");
    };
  }

  @Override
  default ResultSpecification<T> not() {
    return candidate -> {
      Result<Void, String> result = this.evaluate(candidate);
      return result.isOk() ? Result.err("Negated condition was satisfied") : Result.ok(null);
    };
  }
}
