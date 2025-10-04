package com.github.j4c62.pms.property.domain.driver.command;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;

/**
 * Command interface for applying domain commands to a PropertyAggregate.
 *
 * <p>Implementations of this interface encapsulate business operations that can be performed on a property aggregate.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@FunctionalInterface
public interface Command {
  /**
   * Applies this command to the given property aggregate.
   *
   * @param aggregate the property aggregate to apply the command to
   * @return the result of applying the command
   * @since 2025-09-26
   */
  Result<PropertyAggregate, String> applyTo(PropertyAggregate aggregate);
}
