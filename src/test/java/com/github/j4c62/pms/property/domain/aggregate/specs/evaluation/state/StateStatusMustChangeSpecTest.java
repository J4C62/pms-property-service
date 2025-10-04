package com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.state;

import com.github.j4c62.pms.property.domain.aggregate.DataGenerator;
import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.j4c62.pms.property.domain.aggregate.creation.PropertyAggregateFactory.createPropertyAggregate;
import static org.assertj.core.api.Assertions.assertThat;

class StateStatusMustChangeSpecTest extends DataGenerator {

  @Test
  void givenEqualStateWhenEvaluateThenReturnErrorResult() {
    var result =
        StateStatusMustChangeSpec.create(Status.DELETED)
            .evaluate(
                createPropertyAggregate(
                        Id.of(UUID.randomUUID()),
                        getPropertyData(2.0),
                        Status.DELETED,
                        PropertyEvents.empty())
                    .unwrap());

    assertThat(result.unwrapErr()).isEqualTo("Property already has status: DELETED");
  }
}
