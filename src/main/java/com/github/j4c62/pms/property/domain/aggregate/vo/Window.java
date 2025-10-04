package com.github.j4c62.pms.property.domain.aggregate.vo;

public record Window(int number) {
  public static Window of(int number) {
    return new Window(number);
  }
}
