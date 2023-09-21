package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import com.codecool.marsexploration.data.utilities.Interval;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.logger.Logger;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UiMapConfigurationGetter implements MapConfigurationProvider {
  private static final String NAME = "Manual Map Configurator";
  private final Logger logger;
  private final Input input;
  private final TilesManager tiles;
  private MapValidationConfiguration validationConfiguration;
  
  public UiMapConfigurationGetter(Logger logger, Input input, TilesManager tilesManager) {
    this.logger = logger;
    this.input = input;
    this.tiles = tilesManager;
  }
  
  @Override
  public MapConfiguration getMapConfiguration(@NotNull MapValidationConfiguration validationConfiguration) {
    this.validationConfiguration = validationConfiguration;
    int size = getSize();
    
    List<CellType> ranges = getRangeTypes();
    List<Pair<CellType, CellType>> resources = getResourceTypes();
    List<CellType> resourceList = resources.stream().map(Pair::getKey).toList();
    
    tiles.startManagingTiles(size, validationConfiguration, ranges, resourceList);
    logger.logInfo(String.format("The maximum number of all elements: %d", tiles.getRemainingFreeTiles()));
    
    Collection<RangeWithNumbersConfiguration> rangeWithNumbersConfigurations = getRangeConfigurations(ranges);
    Collection<ResourceConfiguration> resourceConfigurations = getResourceConfigurations(resources);
    
    return new MapConfiguration(size, rangeWithNumbersConfigurations, resourceConfigurations);
  }
  
  @Override
  public String getName() {
    return NAME;
  }
  
  private int getSize() {
    return getInt(new Interval<>(validationConfiguration.minimumMapSize(), validationConfiguration.maximumMapSize()),
                  "size of the map");
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
  private Collection<ResourceConfiguration> getResourceConfigurations(
          @NotNull List<Pair<CellType, CellType>> resources) {
    Collection<ResourceConfiguration> resourceConfigurations = new ArrayList<>();
    
    for (Pair<CellType, CellType> resourceType : resources) {
      int numberOfElements = getInt(tiles.getTypeElementInterval(resourceType.getKey()),
                                    String.format("number of %ss", resourceType.getKey().getName()));
      tiles.remove(resourceType.getKey(), numberOfElements);
      resourceConfigurations.add(new ResourceConfiguration(resourceType.getKey(),
                                                           numberOfElements,
                                                           resourceType.getValue()));
    }
    
    return resourceConfigurations;
  }
  
  @NotNull
  private Collection<RangeWithNumbersConfiguration> getRangeConfigurations(@NotNull List<CellType> ranges) {
    Collection<RangeWithNumbersConfiguration> rangeWithNumbersConfigurations = new ArrayList<>();
    
    for (CellType rangeType : ranges) {
      int numberOfElements =
              getInt(tiles.getTypeElementInterval(rangeType), String.format("number of %ss", rangeType.getName()));
      tiles.remove(rangeType, numberOfElements);
      int numberOfRanges =
              getInt(new Interval<>(1, numberOfElements), String.format("number of %sranges", rangeType.getName()));
      rangeWithNumbersConfigurations.add(new RangeWithNumbersConfiguration(rangeType,
                                                                           numberOfElements,
                                                                           numberOfRanges));
    }
    
    return rangeWithNumbersConfigurations;
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
