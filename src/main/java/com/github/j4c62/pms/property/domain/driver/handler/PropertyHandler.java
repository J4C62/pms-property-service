package com.github.j4c62.pms.property.domain.driver.handler;

import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;

/**
 * Domain interface for handling property commands and producing outputs.
 *
 * <p>Implementations of this interface process domain commands and return the result as a
 * PropertyOutput.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@FunctionalInterface
public interface PropertyHandler {
  /**
   * Handles a domain command and produces a PropertyOutput.
   *
   * @param command the command to handle
   * @return the output of handling the command
   * @since 2025-09-26
   */
  PropertyOutput handle(Command command);
}
