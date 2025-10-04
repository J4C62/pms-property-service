package com.github.j4c62.pms.property;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Property microservice application.
 *
 * <p>This class bootstraps the Spring Boot context and initializes all components necessary to
 * handle property-related commands and events.
 *
 * <p>It acts as the root of the service's configuration and component scan.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-06-14
 */
@SpringBootApplication
public class PropertyApplicationService {
  /**
   * Starts the Property application.
   *
   * @param args Command-line arguments passed to the application (if any).
   * @author Jose Antonio (J4c62)
   * @since 2025-06-14
   */
  public static void main(String[] args) {
    SpringApplication.run(PropertyApplicationService.class, args);
  }
}
