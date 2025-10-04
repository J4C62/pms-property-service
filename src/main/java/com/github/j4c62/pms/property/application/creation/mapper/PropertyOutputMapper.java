package com.github.j4c62.pms.property.application.creation.mapper;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for converting PropertyAggregate to PropertyOutput and error outputs.
 *
 * <p>This interface defines the mapping logic for converting a domain aggregate to an output DTO,
 * as well as for creating error outputs for failed operations.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Mapper(
    componentModel = "spring",
    imports = {UUID.class, List.class},
    unmappedTargetPolicy = ReportingPolicy.ERROR)
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface PropertyOutputMapper {
  /**
   * Maps a PropertyAggregate to a PropertyOutput DTO, ignoring errors.
   *
   * @param updatedAggregate the updated property aggregate
   * @return a PropertyOutput DTO
   * @since 2025-09-26
   */
  @Mapping(target = "errors", ignore = true)
  PropertyOutput toPropertyOutput(PropertyAggregate updatedAggregate);

  /**
   * Creates a PropertyOutput DTO representing an error for a given property ID.
   *
   * @param id the property ID
   * @param error the error message
   * @return a PropertyOutput DTO with the error
   * @since 2025-09-26
   */
  default PropertyOutput fromError(Id id, String error) {
    return new PropertyOutput(id, null, List.of(error));
  }
}
