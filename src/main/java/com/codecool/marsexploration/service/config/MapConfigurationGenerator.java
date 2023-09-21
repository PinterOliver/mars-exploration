package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.data.config.RangeWithResource;
import com.codecool.marsexploration.data.config.ResourceConfiguration;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MapConfigurationGenerator implements MapConfigurationProvider {
  private static final String NAME = "Automatic Map Configurator - Random";
  private final Random RANDOM;
  private final TilesManager tiles;
  private MapValidationConfiguration validationConfiguration;
  
  public MapConfigurationGenerator(TilesManager tiles, Random RANDOM) {
    this.tiles = tiles;
    this.RANDOM = RANDOM;
  }
  
  @Override
  public MapConfiguration getMapConfiguration(MapValidationConfiguration validationConfiguration) {
    this.validationConfiguration = validationConfiguration;
    int mapSize = RANDOM.nextInt(validationConfiguration.minimumMapSize(), validationConfiguration.maximumMapSize());
    
    List<CellType> ranges = getRangeTypes();
    List<Pair<CellType, CellType>> resources = getResourceTypes();
    List<CellType> resourceList = resources.stream().map(Pair::getKey).toList();
    
    tiles.startManagingTiles(mapSize, validationConfiguration, ranges, resourceList);
    
    Collection<RangeConfiguration> rangeConfigurations = getRangeConfigurations(ranges);
    Collection<ResourceConfiguration> resourceConfigurations = getResourceConfigurations(resources);
    return new MapConfiguration(mapSize, rangeConfigurations, resourceConfigurations);
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
  private List<CellType> getRangeTypes() {
    return validationConfiguration.rangeTypesWithResources().stream().map(RangeWithResource::rangeType).toList();
  }
  
  @NotNull
  private Collection<RangeConfiguration> getRangeConfigurations(@NotNull List<CellType> ranges) {
    Collection<RangeConfiguration> rangeConfigurations = new ArrayList<>();
    
    for (CellType rangeType : ranges) {
      int numberOfElements = RANDOM.nextInt(tiles.getTypeElementInterval(rangeType).minimum(),
                                            tiles.getTypeElementInterval(rangeType).maximum());
      tiles.remove(rangeType, numberOfElements);
      int numberOfRanges = RANDOM.nextInt(1, Math.max((numberOfElements / 5), 2));
      rangeConfigurations.add(new RangeConfiguration(rangeType, numberOfElements, numberOfRanges));
    }
    
    return rangeConfigurations;
  }
  
  @NotNull
  private Collection<ResourceConfiguration> getResourceConfigurations(
          @NotNull List<Pair<CellType, CellType>> resources) {
    Collection<ResourceConfiguration> resourceConfigurations = new ArrayList<>();
    
    for (Pair<CellType, CellType> resourceType : resources) {
      int numberOfElements = RANDOM.nextInt(tiles.getTypeElementInterval(resourceType.getKey()).minimum(),
                                            tiles.getTypeElementInterval(resourceType.getKey()).maximum());
      tiles.remove(resourceType.getKey(), numberOfElements);
      resourceConfigurations.add(new ResourceConfiguration(resourceType.getKey(),
                                                           numberOfElements,
                                                           resourceType.getValue()));
    }
    
    return resourceConfigurations;
  }
}
