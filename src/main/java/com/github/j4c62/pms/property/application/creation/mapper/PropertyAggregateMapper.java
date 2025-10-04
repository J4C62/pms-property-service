package com.github.j4c62.pms.property.application.creation.mapper;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.driver.command.types.CreatePropertyCommand;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for converting CreatePropertyCommand to PropertyAggregate.
 *
 * <p>This interface defines the mapping logic for creating a new PropertyAggregate from a domain
 * command, including ID generation and ignoring event history at creation time.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Mapper(
    componentModel = "spring",
    imports = {UUID.class},
    unmappedTargetPolicy = ReportingPolicy.ERROR)
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface PropertyAggregateMapper {
  /**
   * Maps a CreatePropertyCommand to a new PropertyAggregate, generating a new ID.
   *
   * @param createPropertyCommand the command to create a property
   * @return a new PropertyAggregate instance
   * @since 2025-09-26
   */
  @Mapping(target = "id", expression = "java(new Id(UUID.randomUUID()))")
  @Mapping(target = "propertyEvents", ignore = true)
  PropertyAggregate toAggregate(CreatePropertyCommand createPropertyCommand);
}
