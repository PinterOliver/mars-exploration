package com.codecool.marsexploration;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.service.filewriter.MapFileWriter;
import com.codecool.marsexploration.service.filewriter.MapFileWriterImpl;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.input.InputImpl;
import com.codecool.marsexploration.service.logger.ConsoleLogger;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.MapManager;
import com.codecool.marsexploration.service.map.MapManagerImpl;
import com.codecool.marsexploration.service.map.MapProvider;
import com.codecool.marsexploration.service.map.ShapeGenerator;
import com.codecool.marsexploration.service.utilities.Pick;
import com.codecool.marsexploration.service.utilities.PickImpl;
import com.codecool.marsexploration.service.validation.MapConfigurationValidator;
import com.codecool.marsexploration.service.validation.MapConfigurationValidatorImpl;
import com.codecool.marsexploration.ui.MarsMapUi;
import com.codecool.marsexploration.visuals.Main;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Application {
  public static final String FILE_PATH = "src/main/resources/maps/exploration.map";
  private static final MapProvider MAP_PROVIDER = null;
  private static final String FILE_PATH_FORMAT = "src/main/resources/maps/exploration-%d.map";
  private static final Logger LOGGER = new ConsoleLogger();
  private static final Scanner SCANNER = new Scanner(System.in);
  private static final Input INPUT = new InputImpl(SCANNER, LOGGER);
  private static final MapFileWriter FILE_WRITER = new MapFileWriterImpl(LOGGER);
  private static final MapManager MAP_MANAGER = new MapManagerImpl(MAP_PROVIDER, FILE_WRITER);
  private static final Random RANDOM = new Random();
  private static final Pick PICK = new PickImpl(RANDOM);
  private static final MapValidationConfiguration VALIDATION_CONFIGURATION = new MapValidationConfiguration(0.3,
                                                                                                            10,
                                                                                                            40,
                                                                                                            0.05,
                                                                                                            0.01,
                                                                                                            List.of(CellType.MOUNTAIN,
                                                                                                                    CellType.PIT),
                                                                                                            List.of(CellType.WATER,
                                                                                                                    CellType.MINERAL));
  private static final MapConfigurationValidator VALIDATOR =
          new MapConfigurationValidatorImpl(VALIDATION_CONFIGURATION);
  private static final MarsMapUi UI =
          new MarsMapUi(LOGGER, INPUT, MAP_MANAGER, VALIDATOR, VALIDATION_CONFIGURATION, FILE_PATH_FORMAT);
  private static final ShapeGenerator SHAPE_GENERATOR = null;
  
  public static void main(String[] args) {
    UI.run();
    Main.main(new String[] {});
  }
}
