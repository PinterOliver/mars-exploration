package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;

public interface MapConfigurationProvider {
  MapConfiguration get(MapValidationConfiguration validationConfiguration);
  String getName();
}
