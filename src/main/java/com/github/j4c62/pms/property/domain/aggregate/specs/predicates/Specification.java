package com.github.j4c62.pms.property.domain.aggregate.specs.predicates;

@FunctionalInterface
public interface Specification<T> {
  boolean isSatisfiedBy(T candidate);

  default Specification<T> and(Specification<T> other) {
    return candidate -> this.isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
  }

  default Specification<T> or(Specification<T> other) {
    return candidate -> this.isSatisfiedBy(candidate) || other.isSatisfiedBy(candidate);
  }

  default Specification<T> not() {
    return candidate -> !this.isSatisfiedBy(candidate);
  }
}
