package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
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
  private int remainingFreeTiles;
  
  public UiMapConfigurationGetter(Logger logger, Input input) {
    this.logger = logger;
    this.input = input;
  }
  
  @Override
  public MapConfiguration get(MapValidationConfiguration validationConfiguration) {
    int size = getInt(validationConfiguration.minimumMapSize(),
                      validationConfiguration.maximumMapSize(),
                      "size of the map");
    
    int mapSize = (int) Math.pow(size, 2);
    remainingFreeTiles = (int) (mapSize * validationConfiguration.maxFilledTilesRatio());
    logger.logInfo(String.format("The maximum number of all elements: %d", remainingFreeTiles));
    int minimumRangeNumber = (int) (mapSize * validationConfiguration.minimumRangeTypeRatio());
    int minimumResourceNumber = (int) (mapSize * validationConfiguration.minimumResourceTypeRatio());
    
    List<CellType> ranges =
            validationConfiguration.rangeTypesWithResources().stream().map(RangeWithResource::rangeType).toList();
    List<Pair<CellType, CellType>> resources = validationConfiguration.rangeTypesWithResources()
                                                                      .stream()
                                                                      .flatMap(range -> range.resourceTypes()
                                                                                             .stream()
                                                                                             .map(resource -> new Pair<>(
                                                                                                     resource,
                                                                                                     range.rangeType())))
                                                                      .toList();
    
    Collection<RangeConfiguration> rangeConfigurations =
            getRangeConfigurations(ranges, minimumRangeNumber, resources, minimumResourceNumber);
    Collection<ResourceConfiguration> resourceConfigurations =
            getResourceConfigurations(resources, minimumResourceNumber);
    
    return new MapConfiguration(size, rangeConfigurations, resourceConfigurations);
  }
  
  @Override
  public String getName() {
    return NAME;
  }
  
  @NotNull
  private Collection<ResourceConfiguration> getResourceConfigurations(@NotNull List<Pair<CellType, CellType>> resources,
          int minimumResourceNumber) {
    Collection<ResourceConfiguration> resourceConfigurations = new ArrayList<>();
    for (int i = 0; i < resources.size(); i++) {
      int maximumResourceNumber = remainingFreeTiles - (resources.size() - i - 1) * minimumResourceNumber;
      int numberOfElements = getInt(minimumResourceNumber,
                                    maximumResourceNumber,
                                    String.format("number of %ss", resources.get(i).getKey().getName()));
      remainingFreeTiles -= numberOfElements;
      resourceConfigurations.add(new ResourceConfiguration(resources.get(i).getKey(),
                                                           numberOfElements,
                                                           resources.get(i).getValue()));
    }
    return resourceConfigurations;
  }
  
  @NotNull
  private Collection<RangeConfiguration> getRangeConfigurations(@NotNull List<CellType> ranges, int minimumRangeNumber,
          List<Pair<CellType, CellType>> resources, int minimumResourceNumber) {
    Collection<RangeConfiguration> rangeConfigurations = new ArrayList<>();
    for (int i = 0; i < ranges.size(); i++) {
      int maximumRangeNumber = remainingFreeTiles - (ranges.size() - i - 1) * minimumRangeNumber -
                               resources.size() * minimumResourceNumber;
      int numberOfElements =
              getInt(minimumRangeNumber, maximumRangeNumber, String.format("number of %ss", ranges.get(i).getName()));
      remainingFreeTiles -= numberOfElements;
      int numberOfRanges = getInt(1, numberOfElements, String.format("number of %sranges", ranges.get(i).getName()));
      rangeConfigurations.add(new RangeConfiguration(ranges.get(i), numberOfElements, numberOfRanges));
    }
    return rangeConfigurations;
  }
  
  private int getInt(int minimumValue, int maximumValue, String message) {
    logger.logInfo(String.format("Enter the %s!", message));
    logger.logInfo(String.format("Validation: [minimum: %d, maximum: %d]", minimumValue, maximumValue));
    return getIntInRange(minimumValue, maximumValue);
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
