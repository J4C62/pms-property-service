package com.github.j4c62.pms.property.domain.aggregate.specs.predicates;

@FunctionalInterface
public interface ParameterizedSpecification<T, P> {
  boolean isSatisfiedBy(T candidate, P parameter);

  default Specification<T> withParameter(P parameter) {
    return candidate -> isSatisfiedBy(candidate, parameter);
  }

  default ParameterizedSpecification<T, P> and(ParameterizedSpecification<T, P> other) {
    return (candidate, param) ->
        this.isSatisfiedBy(candidate, param) && other.isSatisfiedBy(candidate, param);
  }

  default ParameterizedSpecification<T, P> or(ParameterizedSpecification<T, P> other) {
    return (candidate, param) ->
        this.isSatisfiedBy(candidate, param) || other.isSatisfiedBy(candidate, param);
  }

  default ParameterizedSpecification<T, P> not() {
    return (candidate, param) -> !this.isSatisfiedBy(candidate, param);
  }
}
