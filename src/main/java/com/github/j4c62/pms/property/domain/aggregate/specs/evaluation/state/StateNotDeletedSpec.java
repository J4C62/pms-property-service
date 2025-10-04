package com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.state;

import com.github.j4c62.pms.property.domain.aggregate.PropertyAggregate;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.Result;
import com.github.j4c62.pms.property.domain.aggregate.specs.evaluation.ResultSpecification;

public class StateNotDeletedSpec implements ResultSpecification<PropertyAggregate> {

  @Override
  public Result<Void, String> evaluate(PropertyAggregate candidate) {
    return Result.<Void, String>ok(null)
        .filter(ignore -> !candidate.status().isDeleted(), ignore -> "Property is deleted");
  }

  public static ResultSpecification<PropertyAggregate> create() {
    return new StateNotDeletedSpec();
  }
}
