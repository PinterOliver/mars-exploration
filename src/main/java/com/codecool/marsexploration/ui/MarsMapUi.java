package com.codecool.marsexploration.ui;

import com.codecool.marsexploration.Application;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.service.config.MapConfigurationProvider;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.MapManager;
import com.codecool.marsexploration.service.validation.MapConfigurationValidator;
import com.codecool.marsexploration.visuals.Main;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class MarsMapUi {
  private final Logger logger;
  private final Input input;
  private final MapManager manager;
  private final MapConfigurationValidator validator;
  private final MapValidationConfiguration validationConfiguration;
  private final String filePathFormat;
  private final List<MapConfigurationProvider> mapConfigurationProviders;
  private int remainingFreeTiles;
  
  public MarsMapUi(Logger logger, Input input, MapManager mapManager, MapConfigurationValidator validator,
          MapValidationConfiguration validationConfiguration, String filePathFormat,
          List<MapConfigurationProvider> mapConfigurationProviders) {
    this.logger = logger;
    this.input = input;
    this.manager = mapManager;
    this.validator = validator;
    this.validationConfiguration = validationConfiguration;
    this.filePathFormat = filePathFormat;
    this.mapConfigurationProviders = mapConfigurationProviders;
  }
  
  public void run() {
    displayWelcomeMessage();
    runMapGeneration();
    displayMap();
    displayFarewellMessage();
  }
  
  private void displayMap() {
    Main.main(new String[] {});
  }
  
  private void runMapGeneration() {
    if (checkWhetherValidationConfigurationIsValid()) {
      MapConfiguration configuration = getMapConfiguration();
      String filePath = getFilePath();
      Application.setFilePath(filePath);
      manager.createMap(configuration, filePath);
    } else {
      logger.logError("Validation configuration is invalid!");
    }
  }
  
  private boolean checkWhetherValidationConfigurationIsValid() {
    return validationConfiguration.maxFilledTilesRatio() >
           validationConfiguration.minimumRangeTypeRatio() * validationConfiguration.rangeTypesWithResources().size() +
           validationConfiguration.minimumResourceTypeRatio() * validationConfiguration.rangeTypesWithResources()
                                                                                       .stream()
                                                                                       .mapToLong(range -> range.resourceTypes()
                                                                                                                .size())
                                                                                       .sum();
  }
  
  @NotNull
  private MapConfiguration getMapConfiguration() {
    MapConfigurationProvider provider = selectProvider();
    MapConfiguration configuration = provider.get(validationConfiguration);
    while (!validator.isValid(configuration)) {
      logger.logError("Entered configuration is not valid!");
      logger.logInfo("Please enter a valid configuration! Thank you!");
      configuration = provider.get(validationConfiguration);
    }
    return configuration;
  }
  
  private MapConfigurationProvider selectProvider() {
    logger.logInfo("Please enter the index of your chosen map configuration provider!");
    logger.logInfo(mapConfigurationProviders.stream().map(MapConfigurationProvider::getName).toList());
    int index = input.get(Integer::parseInt) - 1;
    while (index < 0 || index >= mapConfigurationProviders.size()) {
      logger.logError("Input out of range");
      logger.logInfo("Please, enter again!");
      index = input.get(Integer::parseInt) - 1;
    }
    return mapConfigurationProviders.get(index);
  }
  
  private String getFilePath() {
    int level = 1;
    String filePath = String.format(filePathFormat, level);
    File file = new File(filePath);
    while (file.exists()) {
      filePath = String.format(filePathFormat, ++level);
      file = new File(filePath);
    }
    return filePath;
  }
  
  private void displayFarewellMessage() {
    logger.logInfo("Goodbye, Young Explorer!");
  }
  
  private void displayWelcomeMessage() {
    logger.logInfo("Welcome, Young Explorer!");
  }
}
