package com.github.j4c62.pms.property.domain.aggregate.specs.validation;

import com.github.j4c62.pms.property.domain.aggregate.specs.predicates.ParameterizedSpecification;

@FunctionalInterface
public interface ParameterizedDataIntegritySpecification<T, P>
    extends ParameterizedSpecification<T, P> {

  default void validate(T candidate, P parameter) {
    if (!isSatisfiedBy(candidate, parameter)) {
      throw new IllegalArgumentException(errorMessage(candidate, parameter));
    }
  }

  default DataIntegritySpecification<T> withParameter(P parameter) {
    return candidate -> isSatisfiedBy(candidate, parameter);
  }

  default String errorMessage(T candidate, P parameter) {
    return "Data integrity validation failed";
  }

  @Override
  boolean isSatisfiedBy(T candidate, P parameter);

  default ParameterizedDataIntegritySpecification<T, P> and(
      ParameterizedDataIntegritySpecification<T, P> other) {
    return (candidate, param) -> {
      if (!this.isSatisfiedBy(candidate, param)) {
        throw new IllegalArgumentException(this.errorMessage(candidate, param));
      }
      if (!other.isSatisfiedBy(candidate, param)) {
        throw new IllegalArgumentException(other.errorMessage(candidate, param));
      }
      return true;
    };
  }

  default ParameterizedDataIntegritySpecification<T, P> or(
      ParameterizedDataIntegritySpecification<T, P> other) {
    return (candidate, param) -> {
      if (this.isSatisfiedBy(candidate, param) || other.isSatisfiedBy(candidate, param)) {
        return true;
      }
      throw new IllegalArgumentException(
          this.errorMessage(candidate, param) + " OR " + other.errorMessage(candidate, param));
    };
  }

  default ParameterizedDataIntegritySpecification<T, P> not() {
    return (candidate, param) -> {
      if (this.isSatisfiedBy(candidate, param)) {
        throw new IllegalArgumentException("Negated condition was satisfied");
      }
      return true;
    };
  }
}
