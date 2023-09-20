package com.codecool.marsexploration.data.utilities;

public record Interval <T extends Number>(T minimum, T maximum) {
  public boolean isValid() {
    return minimum.doubleValue() < maximum.doubleValue();
  }
}
