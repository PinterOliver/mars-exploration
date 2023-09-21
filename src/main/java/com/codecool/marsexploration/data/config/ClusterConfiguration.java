package com.codecool.marsexploration.data.config;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Set;

public record ClusterConfiguration(CellType clusterType, int numberOfElements, int numberOfClusters,
                                   Set<ResourceConfiguration> resourceTypes) {
}
