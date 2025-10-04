package com.github.j4c62.pms.property.application;

import com.github.j4c62.pms.property.domain.driven.PropertyEventPublisher;
import com.github.j4c62.pms.property.domain.driver.command.Command;
import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import com.github.j4c62.pms.property.shared.DrivenFixture;
import com.github.j4c62.pms.property.shared.DriverFixture;
import com.tngtech.jgiven.integration.spring.EnableJGiven;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@TestConfiguration
@ComponentScan("com.github.j4c62.pms.property.application")
@Import({DrivenFixture.class, DriverFixture.class})
@EnableJGiven
public class ApplicationFixture {

  @TestComponent
  public record SetUpFixture(
      PropertyEventPublisher propertyEventPublisher,
      PropertyHandler propertyCommandHandler,
      @Qualifier("createPropertyCommand") Command createPropertyCommand,
      @Qualifier("updateActivePropertyCommand") Command updateActivePropertyCommand,
      @Qualifier("failingCommand") Command failingCommand) {}
}
