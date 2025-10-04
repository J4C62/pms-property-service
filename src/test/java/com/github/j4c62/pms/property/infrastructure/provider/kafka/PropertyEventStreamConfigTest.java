package com.github.j4c62.pms.property.infrastructure.provider.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.j4c62.pms.property.domain.aggregate.event.PropertyEvent;
import com.github.j4c62.pms.property.shared.AggregateFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({AggregateFixture.class, PropertyEventStreamConfig.class})
class PropertyEventStreamConfigTest {
  @Test
  void givenPropertyEventWhenPropertyEventSupplierApplyThenReturnCorrectPayload(
      @Autowired @Qualifier("propertyCreatedEvent") PropertyEvent propertyCreatedEvent,
      @Autowired PropertyEventStreamConfig config) {

    var message = config.propertyEventSupplier().apply(propertyCreatedEvent);
    assertThat(message.getPayload()).isEqualTo(propertyCreatedEvent);
  }
}
