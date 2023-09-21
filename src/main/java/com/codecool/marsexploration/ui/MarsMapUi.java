package com.codecool.marsexploration.ui;

import com.codecool.marsexploration.Application;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.ClusterConfiguration;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.service.config.MapConfigurationProvider;
import com.codecool.marsexploration.service.input.ConfigurationJsonParser;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.MapManager;
import com.codecool.marsexploration.service.map.shape.ShapeGenerator;
import com.codecool.marsexploration.service.map.shape.ShapeGeneratorFactory;
import com.codecool.marsexploration.service.map.shape.ShapeProvider;
import com.codecool.marsexploration.service.validation.MapConfigurationValidator;
import com.codecool.marsexploration.visuals.Main;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class MarsMapUi {
  private final Logger logger;
  private final Input input;
  private final MapManager manager;
  private final MapConfigurationValidator validator;
  private final MapValidationConfiguration validationConfiguration;
  private final String filePathFormat;
  private final List<MapConfigurationProvider> mapConfigurationProviders;
  private final ShapeGeneratorFactory shapeGeneratorFactory;
  
  public MarsMapUi(Logger logger, Input input, MapManager mapManager, MapConfigurationValidator validator,
          ConfigurationJsonParser jsonParser, String filePathFormat,
          List<MapConfigurationProvider> mapConfigurationProviders, ShapeGeneratorFactory shapeGeneratorFactory) {
    this.logger = logger;
    this.input = input;
    this.manager = mapManager;
    this.validator = validator;
    this.validationConfiguration = jsonParser.get();
    this.filePathFormat = filePathFormat;
    this.mapConfigurationProviders = mapConfigurationProviders;
    this.shapeGeneratorFactory = shapeGeneratorFactory;
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
      Map<CellType, ShapeGenerator> shapeGenerators = getShapeGenerators(configuration.clusters());
      String filePath = getFilePath();
      Application.setFilePath(filePath);
      manager.createMap(configuration, filePath, shapeGenerators);
    } else {
      logger.logError("Validation configuration is invalid!");
    }
  }
  
  private Map<CellType, ShapeGenerator> getShapeGenerators(Collection<ClusterConfiguration> clusters) {
    Map<CellType, ShapeGenerator> shapeGenerators = new HashMap<>();
    for (ClusterConfiguration cluster : clusters) {
      ShapeGenerator shapeGenerator = shapeGeneratorFactory.getShapeGenerator(cluster.clusterType());
      shapeGenerators.put(cluster.clusterType(), shapeGenerator);
    }
    return shapeGenerators;
  }
  
  private boolean checkWhetherValidationConfigurationIsValid() {
    return validationConfiguration.maxFilledTilesRatio() >
           validationConfiguration.minimumClusterTypeRatio() * validationConfiguration.clusterTypes().size() +
           validationConfiguration.minimumResourceTypeRatio() *
           validationConfiguration.clusterTypes().stream().mapToLong(cluster -> cluster.resourceTypes().size()).sum();
  }
  
  @NotNull
  private MapConfiguration getMapConfiguration() {
    MapConfigurationProvider provider = selectProvider();
    MapConfiguration configuration = provider.getMapConfiguration(validationConfiguration);
    while (!validator.isValid(configuration, validationConfiguration)) {
      logger.logError("Entered configuration is not valid!");
      logger.logInfo("Please enter a valid configuration! Thank you!");
      configuration = provider.getMapConfiguration(validationConfiguration);
    }
    return configuration;
  }
  
  private MapConfigurationProvider selectProvider() {
    logger.logInfo("Please enter the index of your chosen map configuration provider!");
    logger.logInfo(mapConfigurationProviders.stream().map(MapConfigurationProvider::getName).toList());
    int indexOffset = 1;
    int index = input.get(Integer::parseInt) - indexOffset;
    while (index < 0 || index >= mapConfigurationProviders.size()) {
      logger.logError("Input out of range");
      logger.logInfo("Please, enter again!");
      index = input.get(Integer::parseInt) - indexOffset;
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
