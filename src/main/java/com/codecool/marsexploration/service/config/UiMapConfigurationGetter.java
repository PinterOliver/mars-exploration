package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import com.codecool.marsexploration.data.utilities.Interval;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UiMapConfigurationGetter implements MapConfigurationProvider {
  private static final String NAME = "Manual Map Configurator";
  private final Logger logger;
  private final Input input;
  private final TilesManager tiles;
  
  public UiMapConfigurationGetter(Logger logger, Input input, TilesManager tilesManager) {
    this.logger = logger;
    this.input = input;
    this.tiles = tilesManager;
  }
  
  @Override
  public MapConfiguration getMapConfiguration(@NotNull MapValidationConfiguration validationConfiguration) {
    int size = getSize(validationConfiguration);
    
    tiles.startManagingTiles(size, validationConfiguration);
    logger.logInfo(String.format("The maximum number of all elements: %d", tiles.getRemainingFreeTiles()));
    
    Collection<ClusterConfiguration> clusterConfigurations = getClusterConfigurations(validationConfiguration);
    
    tiles.finishManagingTiles();
    return new MapConfiguration(size, clusterConfigurations);
  }
  
  @Override
  public String getName() {
    return NAME;
  }
  
  private int getSize(@NotNull MapValidationConfiguration validationConfiguration) {
    return getInt(new Interval<>(validationConfiguration.minimumMapSize(), validationConfiguration.maximumMapSize()),
                  "size of the map");
  }
  
  @NotNull
  private Set<ResourceConfiguration> getResourceConfigurations(@NotNull Set<CellType> resources) {
    Set<ResourceConfiguration> resourceConfigurations = new HashSet<>();
    
    for (CellType resourceType : resources) {
      int numberOfElements = getInt(tiles.getTypeElementInterval(resourceType),
                                    String.format("number of %ss", resourceType.getName()));
      tiles.remove(resourceType, numberOfElements);
      resourceConfigurations.add(new ResourceConfiguration(resourceType, numberOfElements));
    }
    
    return resourceConfigurations;
  }
  
  @NotNull
  private Collection<ClusterConfiguration> getClusterConfigurations(
          @NotNull MapValidationConfiguration validationConfiguration) {
    Collection<ClusterConfiguration> clusterConfigurations = new ArrayList<>();
    
    for (Cluster cluster : validationConfiguration.clusterTypes()) {
      CellType clusterType = cluster.clusterType();
      int numberOfElements =
              getInt(tiles.getTypeElementInterval(clusterType), String.format("number of %ss", clusterType.getName()));
      tiles.remove(clusterType, numberOfElements);
      int numberOfClusters =
              getInt(new Interval<>(1, numberOfElements), String.format("number of %sclusters", clusterType.getName()));
      
      Set<CellType> resources = cluster.resourceTypes();
      Set<ResourceConfiguration> resourceConfigurations = getResourceConfigurations(resources);
      
      clusterConfigurations.add(new ClusterConfiguration(clusterType,
                                                         numberOfElements,
                                                         numberOfClusters,
                                                         resourceConfigurations));
    }
    
    return clusterConfigurations;
  }
  
  private int getInt(@NotNull Interval<Integer> interval, String message) {
    logger.logInfo(String.format("Enter the %s!", message));
    logger.logInfo(String.format("Validation: [minimum: %d, maximum: %d]", interval.minimum(), interval.maximum()));
    return getIntInRange(interval.minimum(), interval.maximum());
  }
  
  private int getIntInRange(int minimumValue, int maximumValue) {
    int inputNumber = input.get(Integer::parseInt);
    while (inputNumber < minimumValue || inputNumber > maximumValue) {
      logger.logError("Input out of range");
      logger.logInfo("Please, enter again!");
      inputNumber = input.get(Integer::parseInt);
    }
    return inputNumber;
  }
}
