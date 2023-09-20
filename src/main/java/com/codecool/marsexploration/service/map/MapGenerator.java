package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.RangeConfiguration;
import com.codecool.marsexploration.data.config.ResourceConfiguration;
import com.codecool.marsexploration.data.map.Area;
import com.codecool.marsexploration.data.map.MarsMap;
import com.codecool.marsexploration.data.utilities.Coordinate;
import com.codecool.marsexploration.service.logger.ConsoleLogger;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.shape.ShapeGenerator;

import java.util.*;

public class MapGenerator implements MapProvider {
  private final Random RANDOM = new Random();
  private final Logger log = new ConsoleLogger();
  private MarsMap map;
  private Map<CellType, ShapeGenerator> shapeGenerators;
  private int restarts = 0;
  
  public MapGenerator(Map<CellType, ShapeGenerator> shapeGenerators) {
    this.shapeGenerators = shapeGenerators;
  }
  
  @Override
  public MarsMap generate(MapConfiguration configuration) {
    
    createEmptyMap(configuration.size());
    
    generateShapes(configuration);
    placeResources(configuration);
    
    log.logInfo(map.toString());
    log.logInfo("restarts: " + restarts);
    return map;
  }
  
  private void generateShapes(MapConfiguration configuration) {
    HashMap<CellType, int[]> shapeConfig = generateShapeConfigurations(configuration.ranges());
    createShapes(shapeConfig, configuration.size());
  }
  
  private HashMap<CellType, int[]> generateShapeConfigurations(Collection<RangeConfiguration> configuration) {
    HashMap<CellType, int[]> shapeConfig = new HashMap<>();
    
    for (RangeConfiguration rangeConfig : configuration) {
      shapeConfig.put(rangeConfig.type(),
                      generateShapeSizes(rangeConfig.numberOfRanges(), rangeConfig.numberOfElements()));
    }
    return shapeConfig;
  }
  
  private void createShapes(HashMap<CellType, int[]> shapes, int size) {
    int numberOfShapesGenerated = 0;
    int attemptLimit = 100;
    
    shapeGenerationLoop:
    for (Map.Entry<CellType, int[]> specificShapeSizes : shapes.entrySet()) {
      int placedShapeCounter = 0;
      int attemptsWithCurrentShapeSize = 0;
      ShapeGenerator generator = shapeGenerators.get(specificShapeSizes.getKey());
      while (placedShapeCounter < specificShapeSizes.getValue().length) {
        Area generatedShape = generator.generate(specificShapeSizes.getValue()[placedShapeCounter]);
        
        numberOfShapesGenerated++;
        attemptsWithCurrentShapeSize++;
        
        if (isSuccessfulShapePlacement(generatedShape, specificShapeSizes.getKey())) {
          placedShapeCounter++;
          attemptsWithCurrentShapeSize = 0;
        }
        
        // first version of exiting conditions - more thinking needed
        if (attemptsWithCurrentShapeSize >= attemptLimit) {
          if (restarts < attemptLimit) {
            restartGeneration(shapes, size);
          } else {
            createEmptyMap(size);
            log.logError("Unlucky!");
            // maybe start over with newly generated shape sizes here
          }
          log.logInfo("Breaking!");
          break shapeGenerationLoop;
        }
      }
    }
    log.logInfo("Shapes generated: " + numberOfShapesGenerated);
  }
  
  private void restartGeneration(HashMap<CellType, int[]> shapes, int size) {
    restarts++;
    map = new MarsMap(size);
    createShapes(shapes, size);
  }
  
  private boolean isSuccessfulShapePlacement(Area generatedShape, CellType type) {
    List<Coordinate> possibleStartPoints = generatePossibleStartPoints(generatedShape);
    
    while (!possibleStartPoints.isEmpty()) {
      int startPointIndex = RANDOM.nextInt(possibleStartPoints.size());
      Coordinate startPoint = possibleStartPoints.get(startPointIndex);
      if (validateShapePlacement(startPoint, generatedShape)) {
        placeShape(startPoint, generatedShape, type);
        return true;
      }
      possibleStartPoints.remove(startPointIndex);
    }
    return false;
  }
  
  private void placeShape(Coordinate startPoint, Area generatedShape, CellType type) {
    for (int row = 0; row < generatedShape.getHeight(); row++) {
      for (int column = 0; column < generatedShape.getWidth(); column++) {
        if (generatedShape.getCell(new Coordinate(row, column)).getType().equals(type)) {
          map.setCell(new Coordinate(row + startPoint.row(), column + startPoint.column()), type);
        }
      }
    }
  }
  
  private boolean validateShapePlacement(Coordinate startPoint, Area generatedShape) {
    for (int row = 0; row < generatedShape.getHeight(); row++) {
      for (int column = 0; column < generatedShape.getWidth(); column++) {
        if (!generatedShape.getCell(new Coordinate(row, column)).getType().equals(CellType.EMPTY)) {
          if (!map.getCell(new Coordinate(row + startPoint.row(), column + startPoint.column()))
                  .getType()
                  .equals(CellType.EMPTY)) {
            return false;
          }
        }
      }
    }
    return true;
  }
  
  private List<Coordinate> generatePossibleStartPoints(Area generatedShape) {
    int maxHeightIndex = map.getHeight() - generatedShape.getHeight();
    int maxWidthIndex = map.getWidth() - generatedShape.getWidth();
    
    List<Coordinate> possibleStartPoints = new ArrayList<>();
    
    for (int row = 0; row < maxHeightIndex; row++) {
      for (int column = 0; column < maxWidthIndex; column++) {
        possibleStartPoints.add(new Coordinate(row, column));
      }
    }
    return possibleStartPoints;
  }
  
  private int[] generateShapeSizes(int numberOfShapes, int numberOfTiles) {
    int minimumShapeSizeFactor = 2;
    int minimumShapeSize = numberOfTiles / (numberOfShapes * minimumShapeSizeFactor);
    int remainingTilesToAssign = numberOfTiles;
    int[] shapeSizes = new int[numberOfShapes];
    
    for (int i = 0; i < numberOfShapes - 1; i++) {
      int numberOfShapesToGenerate = numberOfShapes - 2 - i;
      int maximumShapeSize = remainingTilesToAssign - (numberOfShapesToGenerate * minimumShapeSize);
      shapeSizes[i] = RANDOM.nextInt(minimumShapeSize, maximumShapeSize);
      remainingTilesToAssign -= shapeSizes[i];
    }
    shapeSizes[numberOfShapes - 1] = remainingTilesToAssign;
    return shapeSizes;
  }
  
  private void createEmptyMap(int size) {
    this.map = new MarsMap(size);
  }
  
  private void placeResources(MapConfiguration mapConfiguration) {
    for (ResourceConfiguration configuration : mapConfiguration.resources()) {
      
      int numberOfResources = configuration.numberOfElements();
      CellType requiredNeighbor = getRequiredNeighbor(configuration.type());
      ArrayList<Coordinate> emptyCoordinates = getEmptyCells();
      
      while (numberOfResources > 0) {
        int emptyCoordinateIndex = RANDOM.nextInt(emptyCoordinates.size());
        Coordinate randomCoordinate = emptyCoordinates.get(emptyCoordinateIndex);
        emptyCoordinates.remove(emptyCoordinateIndex);
        if (isValidResourcePosition(randomCoordinate, requiredNeighbor, mapConfiguration.size())) {
          map.setCell(randomCoordinate, configuration.type());
          numberOfResources--;
        }
      }
    }
  }
  
  private boolean isValidResourcePosition(Coordinate randomCoordinate, CellType requiredNeighbor, int size) {
    
    Collection<Cell> neighborCells = map.getNeighbours(randomCoordinate, 1);
    
    for (Cell option : neighborCells) {
      if (option.getType().equals(requiredNeighbor)) {
        return true;
      }
    }
    return false;
  }
  
  private ArrayList<Coordinate> getEmptyCells() {
    ArrayList<Coordinate> emptyCoordinates = new ArrayList<>();
    
    for (int row = 0; row < map.getHeight(); row++) {
      for (int column = 0; column < map.getWidth(); column++) {
        if (map.getCell(new Coordinate(row, column)).getType().equals(CellType.EMPTY)) {
          emptyCoordinates.add(new Coordinate(row, column));
        }
      }
    }
    return emptyCoordinates;
  }
  
  private CellType getRequiredNeighbor(CellType type) {
    switch (type) {
      case MINERAL -> {
        return CellType.MOUNTAIN;
      }
      case WATER -> {
        return CellType.PIT;
      }
      default -> {
        return CellType.EMPTY;
      }
    }
  }
}
