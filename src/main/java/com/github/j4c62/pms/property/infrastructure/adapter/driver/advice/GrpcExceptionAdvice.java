package com.github.j4c62.pms.property.infrastructure.adapter.driver.advice;

import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

/**
 * Exception advice for handling gRPC errors in the property service.
 *
 * <p>This class provides centralized exception handling for gRPC controller methods, allowing
 * custom mapping of exceptions to gRPC status codes and error responses.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@GrpcAdvice
@Slf4j
public class GrpcExceptionAdvice {

  @GrpcExceptionHandler({
    IllegalArgumentException.class,
    IllegalStateException.class,
    NullPointerException.class
  })
  public Status handleInvalidArgument(RuntimeException e) {
    if (log.isWarnEnabled()) {
      log.warn(
          "[advice] RuntimeException - type={}, message={}, cause={}",
          e.getClass().getSimpleName(),
          e.getMessage(),
          e.getCause(),
          e);
    }
    return Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(Exception.class)
  public Status handleException(Exception e) {
    if (log.isWarnEnabled()) {
      log.warn("[advice] Critical error: cause:{}", e.getMessage());
    }
    return Status.INTERNAL.withDescription(e.getMessage()).withCause(e);
  }
}
