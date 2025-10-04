package com.github.j4c62.pms.property.infrastructure.adapter.driver.mapper;

import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import com.github.j4c62.pms.property.domain.driver.output.PropertyOutput;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.PropertyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Maps domain PropertyOutput objects to gRPC PropertyResponse messages.
 *
 * <p>This interface uses MapStruct to convert between domain and gRPC representations, including
 * custom mapping for status and error fields.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface PropertyResponseMapper {
  /**
   * Maps a PropertyOutput domain object to a gRPC PropertyResponse message.
   *
   * @param output The domain PropertyOutput object.
   * @return The mapped gRPC PropertyResponse message.
   * @since 2025-09-26
   */
  @Mapping(target = "id", expression = "java(output.id().value().toString())")
  @Mapping(target = "status", expression = "java(mapStatus(output.status()))")
  @Mapping(target = "mergeFrom", ignore = true)
  @Mapping(target = "clearExtension", ignore = true)
  @Mapping(target = "clearField", ignore = true)
  @Mapping(target = "clearOneof", ignore = true)
  @Mapping(target = "idBytes", ignore = true)
  @Mapping(target = "statusBytes", ignore = true)
  @Mapping(target = "unknownFields", ignore = true)
  @Mapping(target = "mergeUnknownFields", ignore = true)
  @Mapping(target = "allFields", ignore = true)
  PropertyResponse toResponse(PropertyOutput output);

  /**
   * Maps a domain Status to its string representation for gRPC.
   *
   * @param status The domain status.
   * @return The status as a string, or empty if null.
   * @since 2025-09-26
   */
  default String mapStatus(Status status) {
    return status != null ? status.name() : "";
  }

  /**
   * Maps errors from PropertyOutput to the gRPC PropertyResponse builder.
   *
   * @param output The domain PropertyOutput.
   * @param builder The gRPC PropertyResponse builder.
   * @since 2025-09-26
   */
  @AfterMapping
  default void mapErrors(PropertyOutput output, @MappingTarget PropertyResponse.Builder builder) {
    if (output.errors() != null && !output.errors().isEmpty()) {
      builder.addAllErrors(output.errors());
    }
  }
}
