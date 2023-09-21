package com.codecool.marsexploration.data.config;

import java.util.Collection;

public record MapConfiguration(int size, Collection<ClusterConfiguration> clusters) {
  public int getNumberOfTiles() {
    return clusters.stream().mapToInt(ClusterConfiguration::numberOfElements).sum() +
           clusters.stream().mapToInt(range -> range.resourceTypes().size()).sum();
  }
}
