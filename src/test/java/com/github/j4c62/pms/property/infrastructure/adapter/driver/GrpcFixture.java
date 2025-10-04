package com.github.j4c62.pms.property.infrastructure.adapter.driver;

import com.github.j4c62.pms.property.infrastructure.adapter.driver.advice.GrpcExceptionAdvice;
import com.github.j4c62.pms.property.shared.DriverFixture;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcAdviceAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({
  GrpcControllerAdapter.class,
  GrpcServerAutoConfiguration.class,
  GrpcServerFactoryAutoConfiguration.class,
  GrpcClientAutoConfiguration.class,
  GrpcAdviceAutoConfiguration.class,
  GrpcExceptionAdvice.class,
  DriverFixture.class,
})
@ComponentScan("com.github.j4c62.pms.property.infrastructure.adapter.driver.mapper")
public class GrpcFixture {}
