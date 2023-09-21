package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MapConfigurationGenerator implements MapConfigurationProvider {
  private static final String NAME = "Automatic Map Configurator - Random";
  private final Random random;
  private final TilesManager tiles;
  
  public MapConfigurationGenerator(TilesManager tiles, Random RANDOM) {
    this.tiles = tiles;
    this.random = RANDOM;
  }
  
  @Override
  public MapConfiguration getMapConfiguration(@NotNull MapValidationConfiguration validationConfiguration) {
    int mapSize = random.nextInt(validationConfiguration.minimumMapSize(), validationConfiguration.maximumMapSize());
    
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
    
    int maximumFloor = 2;
    int minimumAverageClusterSize = 5;
    
    Collection<ClusterConfiguration> clusterConfigurations = new ArrayList<>();
    
    for (Cluster cluster : validationConfiguration.clusterTypes()) {
      CellType clusterType = cluster.clusterType();
      int numberOfElements = random.nextInt(tiles.getTypeElementInterval(clusterType).minimum(),
                                            tiles.getTypeElementInterval(clusterType).maximum());
      tiles.remove(clusterType, numberOfElements);
      
      int maximumNumberOfClusters = Math.max((numberOfElements / minimumAverageClusterSize), maximumFloor);
      int numberOfClusters = random.nextInt(1, maximumNumberOfClusters);
      
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
    double resourceFactor =
            validationConfiguration.minimumResourceTypeRatio() / validationConfiguration.minimumClusterTypeRatio();
    
    Set<ResourceConfiguration> resourceConfigurations = new HashSet<>();
    
    for (CellType resourceType : resources) {
      int numberOfElements = random.nextInt(tiles.getTypeElementInterval(resourceType).minimum(),
                                            (int) Math.min(tiles.getTypeElementInterval(resourceType).maximum(),
                                                           numberOfClusterElements * resourceFactor + 1));
      tiles.remove(resourceType, numberOfElements);
      resourceConfigurations.add(new ResourceConfiguration(resourceType, numberOfElements));
    }
    
    return resourceConfigurations;
  }
}
