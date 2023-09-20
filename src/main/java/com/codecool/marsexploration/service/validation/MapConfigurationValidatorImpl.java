package com.codecool.marsexploration.service.validation;

import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.data.config.RangeConfiguration;
import com.codecool.marsexploration.data.config.ResourceConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.stream.Collectors;

public class MapConfigurationValidatorImpl implements MapConfigurationValidator {
  private final MapValidationConfiguration validationConfiguration;
  
  public MapConfigurationValidatorImpl(@NotNull MapValidationConfiguration validationConfiguration) {
    this.validationConfiguration = validationConfiguration;
  }
  
  @Override
  public boolean isValid(MapConfiguration configuration) {
    if (configuration == null) {
      return false;
    }
    boolean isValid = isMapSizeValid(configuration);
    isValid = isFilledTilesRatioValid(configuration, isValid);
    isValid = areCellTypeListsValid(configuration, isValid);
    isValid = areNumberOfEachCellTypeValid(configuration, isValid);
    isValid = areNumberOfRangesValid(configuration, isValid);
    return isValid;
  }
  
  private boolean areNumberOfRangesValid(MapConfiguration configuration, boolean isValid) {
    isValid = isValid &&
              configuration.ranges().stream().allMatch(range -> range.numberOfElements() >= range.numberOfRanges());
    return isValid;
  }
  
  private boolean areCellTypeListsValid(@NotNull MapConfiguration configuration, boolean isValid) {
    isValid = isValid && configuration.ranges()
                                      .stream()
                                      .map(RangeConfiguration::type)
                                      .collect(Collectors.toSet())
                                      .equals(new HashSet<>(validationConfiguration.rangeTypes()));
    isValid = isValid && configuration.resources()
                                      .stream()
                                      .map(ResourceConfiguration::type)
                                      .collect(Collectors.toSet())
                                      .equals(new HashSet<>(validationConfiguration.resourceTypes()));
    return isValid;
  }
  
  private boolean areNumberOfEachCellTypeValid(@NotNull MapConfiguration configuration, boolean isValid) {
    int minimumRangeNumber = (int) (configuration.size() * validationConfiguration.minimumRangeTypeRatio());
    int minimumResourceNumber = (int) (configuration.size() * validationConfiguration.minimumResourceTypeRatio());
    isValid = isValid && configuration.ranges()
                                      .stream()
                                      .map(RangeConfiguration::numberOfElements)
                                      .allMatch(size -> size >= minimumRangeNumber);
    isValid = isValid && configuration.resources()
                                      .stream()
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
