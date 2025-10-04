package com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.state;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.ParameterizedResultSpecification;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.ResultSpecification;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;

public class StateStatusMustChangeSpec
    implements ParameterizedResultSpecification<PropertyAggregate, Status> {

  @Override
  public Result<Void, String> evaluate(PropertyAggregate candidate, Status status) {
    return Result.<Void, String>ok(null)
        .filter(
            ignore -> !candidate.status().equals(status),
            ignore -> "Property already has status: %s".formatted(status));
  }

  public static ResultSpecification<PropertyAggregate> create(Status status) {
    return new StateStatusMustChangeSpec().withParameter(status);
  }
}
