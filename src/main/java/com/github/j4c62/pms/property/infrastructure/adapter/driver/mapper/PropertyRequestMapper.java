package com.github.j4c62.pms.property.infrastructure.adapter.driver.mapper;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory.createPropertyData;

import com.github.j4c62.pms.property.domain.aggregate.vo.Data;
import com.github.j4c62.pms.property.domain.aggregate.vo.Facility;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.IsolationLevel;
import com.github.j4c62.pms.property.domain.aggregate.vo.LayoutType;
import com.github.j4c62.pms.property.domain.aggregate.vo.LocationType;
import com.github.j4c62.pms.property.domain.aggregate.vo.Type;
import com.github.j4c62.pms.property.domain.driver.command.types.CreatePropertyCommand;
import com.github.j4c62.pms.property.domain.driver.command.types.UpdatePropertyCommand;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.CreatePropertyRequest;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.UpdatePropertyRequest;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Maps gRPC property requests to domain commands for property creation and update.
 *
 * <p>This interface uses MapStruct to convert incoming gRPC requests into domain command objects,
 * including custom mapping for IDs and data fields.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@Mapper(
    componentModel = "spring",
    imports = {UUID.class, List.class},
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PropertyRequestMapper {
  /**
   * Maps a gRPC CreatePropertyRequest to a domain CreatePropertyCommand.
   *
   * @param request The gRPC create property request.
   * @return The mapped domain command.
   * @since 2025-09-26
   */
  @Mapping(target = "data", expression = "java(mapData(request.getData()))")
  CreatePropertyCommand toCreateInput(CreatePropertyRequest request);

  /**
   * Maps a gRPC UpdatePropertyRequest to a domain UpdatePropertyCommand.
   *
   * @param request The gRPC update property request.
   * @return The mapped domain command.
   * @since 2025-09-26
   */
  @Mapping(target = "id", expression = "java(mapId(request.getId()))")
  @Mapping(target = "data", expression = "java(mapData(request.getData()))")
  UpdatePropertyCommand toUpdateInput(UpdatePropertyRequest request);

  /**
   * Maps a string ID to a domain Id object.
   *
   * @param id The string ID.
   * @return The domain Id object, or null if the input is empty.
   * @since 2025-09-26
   */
  default Id mapId(String id) {
    if (id.isEmpty()) {
      throw new IllegalArgumentException("id:'%s' invalid, valid format UUID".formatted(id));
    }
    return Id.of(UUID.fromString(id));
  }

  /**
   * Maps gRPC data object to domain Data object.
   *
   * @param data The gRPC data object.
   * @return The mapped domain Data object.
   * @since 2025-09-26
   */
  default Data mapData(com.github.j4c62.pms.property.infrastructure.provider.grpc.Data data) {

    return createPropertyData(
            data.getName(),
            Type.valueOf(data.getType()),
            data.getCapacity().getMin(),
            data.getCapacity().getMax(),
            data.getSize(),
            data.getCeilingHeight(),
            LayoutType.valueOf(data.getLayout()),
            IsolationLevel.valueOf(data.getIsolation()),
            data.getPrivateEntrance(),
            data.getWindow(),
            data.getClosedRooms(),
            data.getFloor(),
            data.getAccessibility(),
            data.getFacilitiesList().stream().map(Facility::valueOf).collect(Collectors.toSet()),
            data.getLocation().getName(),
            LocationType.valueOf(data.getLocation().getType()),
            data.getPrice().getAmount(),
            Currency.getInstance(data.getPrice().getCurrency()))
        .unwrap();
  }
}
