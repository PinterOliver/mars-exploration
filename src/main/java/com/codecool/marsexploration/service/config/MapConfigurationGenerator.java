package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import com.codecool.marsexploration.service.utilities.Pick;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MapConfigurationGenerator implements MapConfigurationProvider {
  private static final String NAME = "Automatic Map Configurator - Random";
  private final Random RANDOM;
  private final TilesManager tiles;
  private MapValidationConfiguration validationConfiguration;
  
  public MapConfigurationGenerator(TilesManager tiles, Random RANDOM, Pick pick) {
    this.tiles = tiles;
    this.RANDOM = RANDOM;
  }
  
  @Override
  public MapConfiguration getMapConfiguration(MapValidationConfiguration validationConfiguration) {
    this.validationConfiguration = validationConfiguration;
    int mapSize = RANDOM.nextInt(validationConfiguration.minimumMapSize(), validationConfiguration.maximumMapSize());
    
    tiles.startManagingTiles(mapSize, validationConfiguration);
    
    Collection<RangeWithNumbersConfiguration> rangeConfigurations = getRangeConfigurations();
    return new MapConfiguration(mapSize, rangeConfigurations);
  }
  
  @Override
  public String getName() {
    return NAME;
  }
  
  @NotNull
  private List<Pair<CellType, CellType>> getResourceTypes() {
    return validationConfiguration.rangeTypesWithResources()
                                  .stream()
                                  .flatMap(range -> range.resourceTypes()
                                                         .stream()
                                                         .map(resource -> new Pair<>(resource, range.rangeType())))
                                  .toList();
  }
  
  @NotNull
  private Collection<RangeWithNumbersConfiguration> getRangeConfigurations() {
    Collection<RangeWithNumbersConfiguration> rangeConfigurations = new ArrayList<>();
    
    for (RangeWithResource rangeWithResources : validationConfiguration.rangeTypesWithResources()) {
      CellType rangeType = rangeWithResources.rangeType();
      int numberOfElements = RANDOM.nextInt(tiles.getTypeElementInterval(rangeType).minimum(),
                                            tiles.getTypeElementInterval(rangeType).maximum());
      tiles.remove(rangeType, numberOfElements);
      int numberOfRanges = RANDOM.nextInt(1, Math.max((numberOfElements / 5), 2));
      
      Set<CellType> resources = rangeWithResources.resourceTypes();
      Set<ResourceConfiguration> resourceConfigurations = getResourceConfigurations(resources);
      
      rangeConfigurations.add(new RangeWithNumbersConfiguration(rangeType,
                                                                numberOfElements,
                                                                numberOfRanges,
                                                                resourceConfigurations));
    }
    
    return rangeConfigurations;
  }
  
  @NotNull
  private Set<ResourceConfiguration> getResourceConfigurations(@NotNull Set<CellType> resources) {
    Set<ResourceConfiguration> resourceConfigurations = new HashSet<>();
    
    for (CellType resourceType : resources) {
      int numberOfElements = RANDOM.nextInt(tiles.getTypeElementInterval(resourceType).minimum(),
                                            tiles.getTypeElementInterval(resourceType).maximum());
      tiles.remove(resourceType, numberOfElements);
      resourceConfigurations.add(new ResourceConfiguration(resourceType, numberOfElements));
    }
    
    return resourceConfigurations;
  }
}
