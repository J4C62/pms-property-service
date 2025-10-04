package com.github.j4c62.pms.property.infrastructure.provider.kafka.serde;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(PropertyEventSerde.class)
class PropertyEventSerdeTest {
  @MockitoBean private ObjectMapper objectMapper;
  @Autowired private PropertyEventSerde propertyEventSerde;

  @Test
  void givenInvalidDataWhenSerializerThenThrowsSerializationException()
      throws JsonProcessingException {
    var serializer = propertyEventSerde.serializer();
    when(objectMapper.writeValueAsBytes(any())).thenThrow(new JsonProcessingException("boom") {});

    assertThatThrownBy(() -> serializer.serialize("property-topic", null))
        .isInstanceOf(SerializationException.class)
        .hasMessageContaining("Error serializing PropertyEvent");
  }

  @Test
  void givenInvalidDataWhenDeserializerThenThrowsSerializationException() {
    var invalidData = "invalid json".getBytes(StandardCharsets.UTF_8);

    var deserializer = propertyEventSerde.deserializer();

    assertThatThrownBy(() -> deserializer.deserialize("property-topic", invalidData))
        .isInstanceOf(SerializationException.class)
        .hasMessageContaining("Error deserializing PropertyEvent");
  }

  @Test
  void givenUnknownEventTypeWhenDeserializedThenThrowsSerializationException() throws IOException {

    var json =
        """
            {
               "eventType": "UNKNOWN_EVENT"
            }
            """;

    var bytes = json.getBytes(StandardCharsets.UTF_8);

    var node = mock(JsonNode.class);
    var eventTypeNode = mock(JsonNode.class);

    when(objectMapper.readTree(bytes)).thenReturn(node);
    when(node.get("eventType")).thenReturn(eventTypeNode);
    when(eventTypeNode.asText()).thenReturn("UNKNOWN_EVENT");

    assertThatThrownBy(() -> propertyEventSerde.deserializer().deserialize("property-topic", bytes))
        .hasRootCauseInstanceOf(IllegalArgumentException.class)
        .rootCause()
        .hasMessageContaining("Unknown eventType: UNKNOWN_EVENT");
  }
}
