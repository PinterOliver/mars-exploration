package com.codecool.marsexploration;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.service.filewriter.MapFileWriter;
import com.codecool.marsexploration.service.filewriter.MapFileWriterImpl;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.input.InputImpl;
import com.codecool.marsexploration.service.logger.ConsoleLogger;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.MapGenerator;
import com.codecool.marsexploration.service.map.MapProvider;
import com.codecool.marsexploration.service.map.ShapeGenerator;
import com.codecool.marsexploration.service.utilities.Pick;
import com.codecool.marsexploration.service.utilities.PickImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Application {
  private static final Logger LOGGER = new ConsoleLogger();
  private static final Scanner SCANNER = new Scanner(System.in);
  private static final Input INPUT = new InputImpl(SCANNER, LOGGER);
  private static final MapFileWriter FILE_WRITER = new MapFileWriterImpl(LOGGER);
  private static final Random RANDOM = new Random();
  private static final Pick PICK = new PickImpl(RANDOM);
  
  public static void main(String[] args) {

    //create correct shape generators and add them as constructor to map generator + logger
//    ShapeGenerator mountainGenerator = new ShapeGenerator();
//    ShapeGenerator pitGenerator = new ShapeGenerator();
//
//    Map<CellType, ShapeGenerator> shapeGenerators = new HashMap<>();
//
//    shapeGenerators.put(CellType.MOUNTAIN, mountainGenerator);
//    shapeGenerators.put(CellType.PIT, pitGenerator);

    Logger logger = new ConsoleLogger();

    MapProvider mapGenerator = new MapGenerator();

    MapConfiguration mapConfiguration = new MapConfiguration(12);
    mapGenerator.generate(mapConfiguration);
  }
}
