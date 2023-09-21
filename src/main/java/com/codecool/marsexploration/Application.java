package com.codecool.marsexploration;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.data.config.RangeWithResource;
import com.codecool.marsexploration.service.config.*;
import com.codecool.marsexploration.service.filewriter.MapFileWriter;
import com.codecool.marsexploration.service.filewriter.MapFileWriterImpl;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.input.InputImpl;
import com.codecool.marsexploration.service.logger.ConsoleLogger;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.MapGenerator;
import com.codecool.marsexploration.service.map.MapManager;
import com.codecool.marsexploration.service.map.MapManagerImpl;
import com.codecool.marsexploration.service.map.MapProvider;
import com.codecool.marsexploration.service.map.shape.MountainShapeGenerator;
import com.codecool.marsexploration.service.map.shape.PitShapeGenerator;
import com.codecool.marsexploration.service.map.shape.ShapeProvider;
import com.codecool.marsexploration.service.utilities.Pick;
import com.codecool.marsexploration.service.utilities.PickImpl;
import com.codecool.marsexploration.service.validation.MapConfigurationValidator;
import com.codecool.marsexploration.service.validation.MapConfigurationValidatorImpl;
import com.codecool.marsexploration.ui.MarsMapUi;

import java.util.*;

public class Application {
  private static String FILE_PATH = "src/main/resources/maps/exploration.map";
  
  public static String getFilePath() {
    return FILE_PATH;
  }
  
  public static void setFilePath(String filePath) {
    FILE_PATH = filePath;
  }
  
  public static void main(String[] args) {
    String filePathFormat = "src/main/resources/maps/exploration-%d.map";
    Random random = new Random();
    TilesManager tilesManager = new TilesCalculator();
    Logger logger = new ConsoleLogger();
    Scanner scanner = new Scanner(System.in);
    Input input = new InputImpl(scanner, logger);
    List<MapConfigurationProvider> mapConfigurationProviders =
            List.of(new UiMapConfigurationGetter(logger, input, tilesManager),
                    new MapConfigurationGenerator(tilesManager, random));
    MapFileWriter fileWriter = new MapFileWriterImpl(logger);
    Pick pick = new PickImpl(random);
    MapValidationConfiguration validationConfiguration = new MapValidationConfiguration(0.3,
                                                                                        10,
                                                                                        40,
                                                                                        0.05,
                                                                                        0.01,
                                                                                        List.of(new RangeWithResource(
                                                                                                        CellType.MOUNTAIN,
                                                                                                        Set.of(CellType.MINERAL,
                                                                                                               CellType.GOLD)),
                                                                                                new RangeWithResource(
                                                                                                        CellType.PIT,
                                                                                                        Set.of(CellType.WATER))));
    // TODO: Can be more than one validationConfiguration
    MapConfigurationValidator validator = new MapConfigurationValidatorImpl();
    Map<CellType, ShapeProvider> shapeGenerators =
            Map.of(CellType.MOUNTAIN, new MountainShapeGenerator(random), CellType.PIT, new PitShapeGenerator(random));
    MapProvider mapProvider = new MapGenerator(shapeGenerators, random, pick, logger);
    MapManager mapManager = new MapManagerImpl(mapProvider, fileWriter);
    MarsMapUi ui = new MarsMapUi(logger,
                                 input,
                                 mapManager,
                                 validator,
                                 validationConfiguration,
                                 filePathFormat,
                                 mapConfigurationProviders);
    ui.run();
  }
}
