package com.codecool.marsexploration.data.config;

import java.util.Collection;

public record MapConfiguration(int size, Collection<RangeWithNumbersConfiguration> ranges) {
  public int getNumberOfTiles() {
    return ranges.stream().mapToInt(RangeWithNumbersConfiguration::numberOfElements).sum() +
           ranges.stream().mapToInt(range -> range.resourceTypes().size()).sum();
  }
}
