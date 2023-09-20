package com.codecool.marsexploration.data.config;

import java.util.Collection;

public record MapConfiguration(int size, Collection<RangeConfiguration> ranges,
                               Collection<ResourceConfiguration> resources) {
  public int getNumberOfTiles() {
    return ranges.stream().mapToInt(RangeConfiguration::numberOfElements).sum() +
           resources.stream().mapToInt(ResourceConfiguration::numberOfElements).sum();
  }
}
