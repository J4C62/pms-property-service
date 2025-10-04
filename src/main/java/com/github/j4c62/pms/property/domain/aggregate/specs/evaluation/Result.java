package com.github.j4c62.pms.property.domain.aggregate.specs.evaluation;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Result<T, E> permits Result.Ok, Result.Err {
  T getOrElse(Supplier<T> fallback);

  record Ok<T, E>(T value) implements Result<T, E> {
    @Override
    public <R> R fold(
        Function<? super E, ? extends R> onError, Function<? super T, ? extends R> onSuccess) {
      return onSuccess.apply(value);
    }

    @Override
    public T getOrElse(Supplier<T> fallback) {
      return value;
    }
  }

  record Err<T, E>(E error) implements Result<T, E> {
    @Override
    public <R> R fold(
        Function<? super E, ? extends R> onError, Function<? super T, ? extends R> onSuccess) {
      return onError.apply(error);
    }

    @Override
    public T getOrElse(Supplier<T> fallback) {
      return fallback.get();
    }
  }

  static <T, E> Ok<T, E> ok(T value) {
    return new Ok<>(value);
  }

  static <T, E> Err<T, E> err(E error) {
    return new Err<>(error);
  }

  default boolean isOk() {
    return this instanceof Ok;
  }

  default boolean isErr() {
    return this instanceof Err;
  }

  default Result<T, E> peek(Consumer<? super T> action) {
    if (this instanceof Ok<T, E>(T value)) {
      action.accept(value);
    }
    return this;
  }

  abstract <R> R fold(
      Function<? super E, ? extends R> onError, Function<? super T, ? extends R> onSuccess);

  default Result<T, E> filter(Predicate<T> predicate, Function<T, E> errorProvider) {
    return switch (this) {
      case Ok<T, E>(T value) ->
          predicate.test(value) ? this : Result.err(errorProvider.apply(value));
      case Err<T, E>(E error) -> this;
    };
  }

  default T unwrap() {
    return switch (this) {
      case Ok<T, E>(T value) -> value;
      case Err<T, E> err -> throw new RuntimeException("Tried to unwrap Err: " + err.error());
    };
  }

  default E unwrapErr() {
    return switch (this) {
      case Err<T, E>(E error) -> error;
      case Ok<T, E> ok -> throw new RuntimeException("Tried to unwrapErr Ok: " + ok.value());
    };
  }

  default <U> Result<U, E> map(Function<T, U> mapper) {
    return switch (this) {
      case Ok<T, E>(T value) -> Result.ok(mapper.apply(value));
      case Err<T, E>(E error) -> Result.err(error);
    };
  }

  default <F> Result<T, F> mapErr(Function<E, F> mapper) {
    return switch (this) {
      case Ok<T, E>(T value) -> Result.ok(value);
      case Err<T, E>(E error) -> Result.err(mapper.apply(error));
    };
  }

  default <U> Result<U, E> flatMap(Function<T, Result<U, E>> binder) {
    return switch (this) {
      case Ok<T, E>(T value) -> binder.apply(value);
      case Err<T, E>(E error) -> Result.err(error);
    };
  }
}
