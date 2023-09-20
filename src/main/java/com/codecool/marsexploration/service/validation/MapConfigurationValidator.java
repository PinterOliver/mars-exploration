package com.codecool.marsexploration.service.validation;

import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import org.jetbrains.annotations.NotNull;

public interface MapConfigurationValidator {
  boolean isValid(MapConfiguration configuration, @NotNull MapValidationConfiguration validationConfiguration);
}
