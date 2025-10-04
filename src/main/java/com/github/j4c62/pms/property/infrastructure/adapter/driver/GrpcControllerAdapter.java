package com.github.j4c62.pms.property.infrastructure.adapter.driver;

import com.github.j4c62.pms.property.domain.driver.handler.PropertyHandler;
import com.github.j4c62.pms.property.infrastructure.adapter.driver.mapper.PropertyRequestMapper;
import com.github.j4c62.pms.property.infrastructure.adapter.driver.mapper.PropertyResponseMapper;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.CreatePropertyRequest;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.PropertyResponse;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.PropertyServiceGrpc;
import com.github.j4c62.pms.property.infrastructure.provider.grpc.UpdatePropertyRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * gRPC controller adapter for property service operations.
 *
 * <p>This class implements the gRPC service for property creation and update, mapping requests and
 * responses to the domain layer and handling communication with the PropertyHandler.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GrpcControllerAdapter extends PropertyServiceGrpc.PropertyServiceImplBase {
  private final PropertyHandler propertyHandler;
  private final PropertyRequestMapper propertyRequestMapper;
  private final PropertyResponseMapper propertyResponseMapper;

  /**
   * Handles the gRPC create property request.
   *
   * @param request The gRPC create property request.
   * @param responseObserver The gRPC response observer.
   * @since 2025-09-26
   */
  @Override
  public void createProperty(
      CreatePropertyRequest request, StreamObserver<PropertyResponse> responseObserver) {
    var input = propertyRequestMapper.toCreateInput(request);
    var output = propertyHandler.handle(input);

    var response = propertyResponseMapper.toResponse(output);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void updateProperty(
      UpdatePropertyRequest request, StreamObserver<PropertyResponse> responseObserver) {
    var input = propertyRequestMapper.toUpdateInput(request);
    var output = propertyHandler.handle(input);
    var response = propertyResponseMapper.toResponse(output);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
