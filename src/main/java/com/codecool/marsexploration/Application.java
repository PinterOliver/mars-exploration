package com.codecool.marsexploration;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.service.config.*;
import com.codecool.marsexploration.service.filewriter.MapFileWriter;
import com.codecool.marsexploration.service.filewriter.MapFileWriterImpl;
import com.codecool.marsexploration.service.input.*;
import com.codecool.marsexploration.service.logger.ConsoleLogger;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.MapGenerator;
import com.codecool.marsexploration.service.map.MapManager;
import com.codecool.marsexploration.service.map.MapManagerImpl;
import com.codecool.marsexploration.service.map.MapProvider;
import com.codecool.marsexploration.service.map.shape.*;
import com.codecool.marsexploration.service.utilities.Pick;
import com.codecool.marsexploration.service.utilities.PickImpl;
import com.codecool.marsexploration.service.validation.MapConfigurationValidator;
import com.codecool.marsexploration.service.validation.MapConfigurationValidatorImpl;
import com.codecool.marsexploration.ui.MarsMapUi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Application {
  private static String FILE_PATH = "src/main/resources/maps/exploration.map";
  
  public static String getFilePath() {
    return FILE_PATH;
  }
  
  public static void setFilePath(String filePath) {
    FILE_PATH = filePath;
  }
  
  public static void main(String[] args) {
    Logger logger = new ConsoleLogger();
    try {
      MarsMapUi ui = getMarsMapUi(logger);
      ui.run();
    } catch (Exception e) {
      logger.logError(e.getMessage());
    }
  }
  
  @NotNull
  private static MarsMapUi getMarsMapUi(Logger logger) throws FileNotFoundException {
    Random random = new Random();
    Input input = getInput(logger);
    String filePathFormat = "src/main/resources/maps/exploration-%d.map";
    List<MapConfigurationProvider> configurationProviders = getConfigurationProviders(logger, input, random);
    ConfigurationJsonParser jsonParser = getJsonParser();
    MapConfigurationValidator validator = new MapConfigurationValidatorImpl();
    MapManager mapManager = getManager(random, logger);
    ShapeGeneratorProvider shapeGeneratorFactory = new ShapeGeneratorProvider(random);
    return new MarsMapUi(logger, input, mapManager, validator, jsonParser, filePathFormat, configurationProviders, shapeGeneratorFactory);
  }
  
  @NotNull
  private static Input getInput(Logger logger) {
    Scanner scanner = new Scanner(System.in);
    return new InputImpl(scanner, logger);
  }
  
  @NotNull
  private static @Unmodifiable List<MapConfigurationProvider> getConfigurationProviders(Logger logger, Input input,
          Random random) {
    TilesManager tilesManager = new TilesCalculator();
    return List.of(new UiMapConfigurationGetter(logger, input, tilesManager),
                   new MapConfigurationGenerator(tilesManager, random));
  }
  
  @NotNull
  private static ConfigurationJsonParser getJsonParser() throws FileNotFoundException {
    String configurationFilePath = "src/main/resources/configuration.json";
    File file = new File(configurationFilePath);
    Scanner configurationScanner = new Scanner(file);
    FileReader fileReader = new FileReaderImpl();
    return new ConfigurationJsonParserImpl(configurationScanner, fileReader);
  }
  
  @NotNull
  private static MapManager getManager(Random random, Logger logger) {
    MapFileWriter fileWriter = new MapFileWriterImpl(logger);
    Pick pick = new PickImpl(random);
    Map<CellType, ShapeProvider> shapeGenerators = Map.of(CellType.MOUNTAIN,
                                                          new MountainShapeGenerator(random, CellType.MOUNTAIN),
                                                          CellType.PIT,
                                                          new PitShapeGenerator(random, CellType.PIT),
                                                          CellType.WATER,
                                                          new LakeShapeGenerator(random, CellType.WATER));
    MapProvider mapProvider = new MapGenerator(random, pick);
    return new MapManagerImpl(mapProvider, fileWriter);
  }
}
