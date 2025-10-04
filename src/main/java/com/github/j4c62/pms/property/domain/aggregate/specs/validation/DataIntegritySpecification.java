package com.github.j4c62.pms.property.domain.aggregate.specs.validation;

import com.github.j4c62.pms.property.domain.aggregate.specs.predicates.Specification;
import java.util.function.Predicate;


@FunctionalInterface
public interface DataIntegritySpecification<T> extends Specification<T> {

  default void validate(T candidate) {
    if (!isSatisfiedBy(candidate)) {
      throw new IllegalArgumentException(errorMessage(candidate));
    }
  }

  default String errorMessage(T candidate) {
    return "Data integrity validation failed";
  }

  @Override
  boolean isSatisfiedBy(T candidate);

  default DataIntegritySpecification<T> and(DataIntegritySpecification<T> other) {
    return candidate -> {
      if (!this.isSatisfiedBy(candidate)) {
        throw new IllegalArgumentException(this.errorMessage(candidate));
      }
      if (!other.isSatisfiedBy(candidate)) {
        throw new IllegalArgumentException(other.errorMessage(candidate));
      }
      return true;
    };
  }

  default DataIntegritySpecification<T> or(DataIntegritySpecification<T> other) {
    return candidate -> {
      if (this.isSatisfiedBy(candidate) || other.isSatisfiedBy(candidate)) {
        return true;
      }
      throw new IllegalArgumentException(
          this.errorMessage(candidate) + " OR " + other.errorMessage(candidate));
    };
  }

  default DataIntegritySpecification<T> not() {
    return candidate -> {
      if (this.isSatisfiedBy(candidate)) {
        throw new IllegalArgumentException("Negated condition was satisfied");
      }
      return true;
    };
  }

  static <T> DataIntegritySpecification<T> of(Predicate<T> predicate, String errorMessage) {
    return new DataIntegritySpecification<T>() {
      @Override
      public boolean isSatisfiedBy(T candidate) {
        return predicate.test(candidate);
      }

      @Override
      public String errorMessage(T candidate) {
        return errorMessage;
      }
    };
  }
}
