package com.github.j4c62.pms.property.application.distpacher;

import static java.util.concurrent.CompletableFuture.runAsync;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.driven.PropertyEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Dispatches property events asynchronously using the domain event publisher.
 *
 * <p>This component takes a PropertyAggregate and publishes all its events asynchronously using the
 * provided PropertyEventPublisher.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Component
public record PropertyEventDispatcher(PropertyEventPublisher propertyEventPublisher) {

  /**
   * Dispatches all events from the given PropertyAggregate asynchronously.
   *
   * @param aggregate the property aggregate whose events should be published
   * @since 2025-09-26
   */
  public void dispatch(PropertyAggregate aggregate) {
    aggregate
        .propertyEvents()
        .events()
        .forEach(event -> runAsync(() -> propertyEventPublisher.publish(event)));
  }
}
