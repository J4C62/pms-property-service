package com.github.j4c62.pms.property.infrastructure.provider.kafka.serde;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({PropertyEventsSerde.class, PropertyEventSerde.class, JacksonAutoConfiguration.class})
class PropertyEventsSerdeSerdeTest {

  @Autowired private PropertyEventsSerde propertyEventsSerde;

  @Test
  void givenInvalidDataWhenSerializerThenThrowsSerializationException() {
    var serializer = propertyEventsSerde.serializer();

    assertThatThrownBy(() -> serializer.serialize("property-topic", null))
        .isInstanceOf(SerializationException.class)
        .hasMessageContaining("Error serializing PropertyEvents");
  }

  @Test
  void givenInvalidDataWhenDeserializerThenThrowsSerializationException() {
    byte[] invalidData = "invalid json".getBytes(StandardCharsets.UTF_8);

    var deserializer = propertyEventsSerde.deserializer();

    assertThatThrownBy(() -> deserializer.deserialize("property-topic", invalidData))
        .isInstanceOf(SerializationException.class)
        .hasMessageContaining("Error deserializing PropertyEvents");
  }
}
