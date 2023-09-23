package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MapConfigurationDefault implements MapConfigurationProvider{
  private static final String NAME = "Automatic Map Configurator - Default";
  private final TilesManager tiles;
  
  public MapConfigurationDefault(TilesManager tiles) {
    this.tiles = tiles;
  }
  
  @Override
  public MapConfiguration getMapConfiguration(@NotNull MapValidationConfiguration validationConfiguration) {
    int mapSize = 30;
    
    tiles.startManagingTiles(mapSize, validationConfiguration);
    
    Collection<ClusterConfiguration> clusterConfigurations = generateClusterConfigurations(validationConfiguration);
    
    tiles.finishManagingTiles();
    return new MapConfiguration(mapSize, clusterConfigurations);
  }
  
  @Override
  public String getName() {
    return NAME;
  }
  
  @NotNull
  private Collection<ClusterConfiguration> generateClusterConfigurations(
          @NotNull MapValidationConfiguration validationConfiguration) {
    
    Collection<ClusterConfiguration> clusterConfigurations = new ArrayList<>();
    int numberOfClusterTypes = validationConfiguration.clusterTypes().size();
    int clusterTypesGenerated = 0;
    
    for (Cluster cluster : validationConfiguration.clusterTypes()) {
      CellType clusterType = cluster.clusterType();
      int totalNumberOfTiles = tiles.getTypeElementInterval(clusterType).maximum();
      int numberOfElements = totalNumberOfTiles / (numberOfClusterTypes - clusterTypesGenerated);
      tiles.remove(clusterType, numberOfElements);
      
      clusterTypesGenerated++;
      int numberOfClusters = 4;
      
      Set<CellType> resources = cluster.resourceTypes();
      Set<ResourceConfiguration> resourceConfigurations =
              generateResourceConfigurations(resources, numberOfElements, validationConfiguration);
      
      clusterConfigurations.add(new ClusterConfiguration(clusterType,
                                                         numberOfElements,
                                                         numberOfClusters,
                                                         resourceConfigurations));
    }
    
    return clusterConfigurations;
  }
  
  @NotNull
  private Set<ResourceConfiguration> generateResourceConfigurations(@NotNull Set<CellType> resources,
          int numberOfClusterElements, MapValidationConfiguration validationConfiguration) {
    
    Set<ResourceConfiguration> resourceConfigurations = new HashSet<>();
    
    for (CellType resourceType : resources) {
      int minimum = tiles.getTypeElementInterval(resourceType).minimum();
      int maximum = tiles.getTypeElementInterval(resourceType).maximum();
      
      int extraResourceFactor = 4;
      int extra = (maximum - minimum) / extraResourceFactor;
      
      int numberOfElements = minimum + extra;
      
      tiles.remove(resourceType, numberOfElements);
      resourceConfigurations.add(new ResourceConfiguration(resourceType, numberOfElements));
    }
    
    return resourceConfigurations;
  }
}
