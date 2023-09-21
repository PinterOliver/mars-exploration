package com.codecool.marsexploration.data.config;

import java.util.List;

public record MapValidationConfiguration(double maxFilledTilesRatio, int minimumMapSize, int maximumMapSize,
                                         double minimumClusterTypeRatio, double minimumResourceTypeRatio,
                                         List<Cluster> clusterTypes) {
}
