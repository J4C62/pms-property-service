package com.github.j4c62.pms.property.domain.aggregate.vo;

public enum Status {
  ACTIVE,
  INACTIVE,
  DELETED;

  public boolean isDeleted() {
    return this == DELETED;
  }
}
