package com.codecool.marsexploration.ui;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.data.config.RangeConfiguration;
import com.codecool.marsexploration.data.config.ResourceConfiguration;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.MapManager;
import com.codecool.marsexploration.service.validation.MapConfigurationValidator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MarsMapUi {
  private final Logger logger;
  private final Input input;
  private final MapManager manager;
  private final MapConfigurationValidator validator;
  private final MapValidationConfiguration validationConfiguration;
  private final String filePathFormat;
  private int remainingFreeTiles;
  
  public MarsMapUi(Logger logger, Input input, MapManager mapManager, MapConfigurationValidator validator,
          MapValidationConfiguration validationConfiguration, String filePathFormat) {
    this.logger = logger;
    this.input = input;
    this.manager = mapManager;
    this.validator = validator;
    this.validationConfiguration = validationConfiguration;
    this.filePathFormat = filePathFormat;
  }
  
  
  @NotNull
  private Collection<ResourceConfiguration> getResourceConfigurations(@NotNull List<CellType> resources,
          int minimumResourceNumber) {
    Collection<ResourceConfiguration> resourceConfigurations = new ArrayList<>();
    for (int i = 0; i < resources.size(); i++) {
      int maximumResourceNumber = remainingFreeTiles - (resources.size() - i - 1) * minimumResourceNumber;
      int numberOfElements = getInt(minimumResourceNumber,
                                    maximumResourceNumber,
                                    String.format("number of %ss", resources.get(i).getName()));
      remainingFreeTiles -= numberOfElements;
      resourceConfigurations.add(new ResourceConfiguration(resources.get(i), numberOfElements));
    }
    return resourceConfigurations;
  }
  private void displayFarewellMessage() {
    logger.logInfo("Goodbye, Young Explorer!");
  }
  
  private void displayWelcomeMessage() {
    logger.logInfo("Welcome, Young Explorer!");
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
      inputNumber = input.get(Integer::parseInt);
    }
    return inputNumber;
  }
}
