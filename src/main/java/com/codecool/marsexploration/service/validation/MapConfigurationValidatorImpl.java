package com.codecool.marsexploration.service.validation;

import com.codecool.marsexploration.data.config.*;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class MapConfigurationValidatorImpl implements MapConfigurationValidator {
  private MapValidationConfiguration validationConfiguration;
  
  @Override
  public boolean isValid(MapConfiguration configuration, @NotNull MapValidationConfiguration validationConfiguration) {
    if (configuration == null) {
      return false;
    }
    this.validationConfiguration = validationConfiguration;
    boolean isValid = isMapSizeValid(configuration);
    isValid = isFilledTilesRatioValid(configuration, isValid);
    isValid = areCellTypeListsValid(configuration, isValid);
    isValid = areNumberOfEachCellTypeValid(configuration, isValid);
    isValid = areNumberOfClustersValid(configuration, isValid);
    return isValid;
  }
  
  private boolean areNumberOfClustersValid(MapConfiguration configuration, boolean isValid) {
    isValid = isValid && configuration.clusters()
                                      .stream()
                                      .allMatch(cluster -> cluster.numberOfElements() >= cluster.numberOfClusters());
    return isValid;
  }
  
  private boolean areCellTypeListsValid(@NotNull MapConfiguration configuration, boolean isValid) {
    isValid = isValid && configuration.clusters()
                                      .stream()
                                      .map(ClusterConfiguration::clusterType)
                                      .collect(Collectors.toSet())
                                      .equals(validationConfiguration.clusterTypes()
                                                                     .stream()
                                                                     .map(Cluster::clusterType)
                                                                     .collect(Collectors.toSet()));
    isValid = isValid && configuration.clusters()
                                      .stream()
                                      .flatMap(cluster -> cluster.resourceTypes().stream())
                                      .map(ResourceConfiguration::resourceType)
                                      .collect(Collectors.toSet())
                                      .equals(validationConfiguration.clusterTypes()
                                                                     .stream()
                                                                     .flatMap(cluster -> cluster.resourceTypes()
                                                                                                .stream())
                                                                     .collect(Collectors.toSet()));
    return isValid;
  }
  
  private boolean areNumberOfEachCellTypeValid(@NotNull MapConfiguration configuration, boolean isValid) {
    int minimumClusterNumber = (int) (configuration.size() * validationConfiguration.minimumClusterTypeRatio());
    int minimumResourceNumber = (int) (configuration.size() * validationConfiguration.minimumResourceTypeRatio());
    isValid = isValid && configuration.clusters()
                                      .stream()
                                      .map(ClusterConfiguration::numberOfElements)
                                      .allMatch(size -> size >= minimumClusterNumber);
    isValid = isValid && configuration.clusters()
                                      .stream()
                                      .flatMap(cluster -> cluster.resourceTypes().stream())
                                      .map(ResourceConfiguration::numberOfElements)
                                      .allMatch(size -> size >= minimumResourceNumber);
    return isValid;
  }
  
  private boolean isFilledTilesRatioValid(@NotNull MapConfiguration configuration, boolean isValid) {
    double numberOfTiles = Math.pow(configuration.size(), 2);
    double numberOfFilledTiles = configuration.getNumberOfTiles();
    return isValid && numberOfFilledTiles / numberOfTiles <= validationConfiguration.maxFilledTilesRatio();
  }
  
  private boolean isMapSizeValid(@NotNull MapConfiguration configuration) {
    return configuration.size() >= validationConfiguration.minimumMapSize() &&
           configuration.size() <= validationConfiguration.maximumMapSize();
  }
}
