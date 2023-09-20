package com.codecool.marsexploration.ui;

import com.codecool.marsexploration.Application;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.MapManager;
import com.codecool.marsexploration.service.validation.MapConfigurationValidator;
import javafx.util.Pair;
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
  
  public void run() {
    displayWelcomeMessage();
    if (checkWhetherValidationConfigurationIsValid()) {
      MapConfiguration configuration = getMapConfiguration();
      String filePath = getFilePath();
      Application.setFilePath(filePath);
      manager.createMap(configuration, filePath);
    } else {
      logger.logError("Validation configuration is invalid!");
    }
    displayFarewellMessage();
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
    MapConfiguration configuration = createMapConfiguration();
    while (!validator.isValid(configuration)) {
      logger.logError("Entered configuration is not valid!");
      logger.logInfo("Please enter a valid configuration! Thank you!");
      configuration = createMapConfiguration();
    }
    return configuration;
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
  
  @NotNull
  private MapConfiguration createMapConfiguration() {
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
      logger.logInfo("Please, enter again!");
      inputNumber = input.get(Integer::parseInt);
    }
    return inputNumber;
  }
}
