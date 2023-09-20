package com.codecool.marsexploration;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
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
import com.codecool.marsexploration.visuals.Main;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Application {
  public static final String FILE_PATH = "src/main/resources/maps/exploration.map";
  private static final String FILE_PATH_FORMAT = "src/main/resources/maps/exploration-%d.map";
  private static final Logger LOGGER = new ConsoleLogger();
  private static final Scanner SCANNER = new Scanner(System.in);
  private static final Input INPUT = new InputImpl(SCANNER, LOGGER);
  private static final MapFileWriter FILE_WRITER = new MapFileWriterImpl(LOGGER);
  private static final Random RANDOM = new Random();
  private static final Pick PICK = new PickImpl(RANDOM);
  private static final MapValidationConfiguration VALIDATION_CONFIGURATION = new MapValidationConfiguration(0.3,
                                                                                                            10,
                                                                                                            40,
                                                                                                            0.05,
                                                                                                            0.01,
                                                                                                            List.of(new RangeWithResource(
                                                                                                                            CellType.MOUNTAIN,
                                                                                                                            Set.of(CellType.MINERAL)),
                                                                                                                    new RangeWithResource(
                                                                                                                            CellType.PIT,
                                                                                                                            Set.of(CellType.WATER))));
  private static final MapConfigurationValidator VALIDATOR =
          new MapConfigurationValidatorImpl(VALIDATION_CONFIGURATION);
  private static final Map<CellType, ShapeProvider> SHAPE_GENERATORS =
          Map.of(CellType.MOUNTAIN, new MountainShapeGenerator(RANDOM), CellType.PIT, new PitShapeGenerator(RANDOM));
  private static final MapProvider MAP_PROVIDER = new MapGenerator(SHAPE_GENERATORS);
  private static final MapManager MAP_MANAGER = new MapManagerImpl(MAP_PROVIDER, FILE_WRITER);
  private static final MarsMapUi UI =
          new MarsMapUi(LOGGER, INPUT, MAP_MANAGER, VALIDATOR, VALIDATION_CONFIGURATION, FILE_PATH_FORMAT);
  
  public static void main(String[] args) {
    UI.run();
    Main.main(new String[] {});
  }
}
