package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MapConfigurationGenerator implements MapConfigurationProvider {
  private static final String NAME = "Automatic Map Configurator - Random";
  private final Random random;
  private final TilesManager tiles;
  
  public MapConfigurationGenerator(TilesManager tiles, Random random) {
    this.tiles = tiles;
    this.random = random;
  }
  
  @Override
  public MapConfiguration getMapConfiguration(@NotNull MapValidationConfiguration validationConfiguration) {
    int mapSize = random.nextInt(validationConfiguration.minimumMapSize(), validationConfiguration.maximumMapSize());
    
    tiles.startManagingTiles(mapSize, validationConfiguration);
    
    Collection<RangeWithNumbersConfiguration> rangeConfigurations =
            generateRangeConfigurations(validationConfiguration);
    
    tiles.finishManagingTiles();
    return new MapConfiguration(mapSize, rangeConfigurations);
  }
  
  @Override
  public String getName() {
    return NAME;
  }
  
  @NotNull
  private Collection<RangeWithNumbersConfiguration> generateRangeConfigurations(
          @NotNull MapValidationConfiguration validationConfiguration) {
    Collection<RangeWithNumbersConfiguration> rangeConfigurations = new ArrayList<>();
    
    for (RangeWithResource rangeWithResources : validationConfiguration.rangeTypesWithResources()) {
      CellType rangeType = rangeWithResources.rangeType();
      int numberOfElements = random.nextInt(tiles.getTypeElementInterval(rangeType).minimum(),
                                            tiles.getTypeElementInterval(rangeType).maximum());
      tiles.remove(rangeType, numberOfElements);
      int numberOfRanges = random.nextInt(1, Math.max((numberOfElements / 5), 2));
      
      Set<CellType> resources = rangeWithResources.resourceTypes();
      Set<ResourceConfiguration> resourceConfigurations = generateResourceConfigurations(resources);
      
      rangeConfigurations.add(new RangeWithNumbersConfiguration(rangeType,
                                                                numberOfElements,
                                                                numberOfRanges,
                                                                resourceConfigurations));
    }
    
    return rangeConfigurations;
  }
  
  @NotNull
  private Set<ResourceConfiguration> generateResourceConfigurations(@NotNull Set<CellType> resources) {
    Set<ResourceConfiguration> resourceConfigurations = new HashSet<>();
    
    for (CellType resourceType : resources) {
      int numberOfElements = random.nextInt(tiles.getTypeElementInterval(resourceType).minimum(),
                                            tiles.getTypeElementInterval(resourceType).maximum());
      tiles.remove(resourceType, numberOfElements);
      resourceConfigurations.add(new ResourceConfiguration(resourceType, numberOfElements));
    }
    
    return resourceConfigurations;
  }
}
