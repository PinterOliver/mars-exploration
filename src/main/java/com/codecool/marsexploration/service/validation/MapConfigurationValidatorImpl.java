package com.codecool.marsexploration.service.validation;

import com.codecool.marsexploration.data.config.MapConfiguration;

public class MapConfigurationValidatorImpl implements MapConfigurationValidator {
  private final double maxFilledTilesRatio;
  
  public MapConfigurationValidatorImpl(double maxFilledTilesRatio) {
    this.maxFilledTilesRatio = maxFilledTilesRatio;
  }
  
  @Override
  public boolean isValid(MapConfiguration configuration) {
    if (configuration == null) {
      return false;
    }
    double numberOfTiles = Math.pow(configuration.size(), 2);
    double numberOfFilledTiles = configuration.getNumberOfTiles();
    return numberOfFilledTiles / numberOfTiles < maxFilledTilesRatio;
  }
}
