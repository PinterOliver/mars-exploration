package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.RangeConfiguration;
import com.codecool.marsexploration.data.config.ResourceConfiguration;
import com.codecool.marsexploration.data.map.Area;
import com.codecool.marsexploration.data.map.MarsMap;
import com.codecool.marsexploration.data.map.ShapeBlueprint;
import com.codecool.marsexploration.data.utilities.Coordinate;
import com.codecool.marsexploration.service.logger.ConsoleLogger;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.shape.ShapeProvider;

import java.util.*;
import java.util.stream.Collectors;

public class MapGenerator implements MapProvider {
  private final Random RANDOM = new Random();
  private final Logger log = new ConsoleLogger();
  private MarsMap map;
  private Map<CellType, ShapeProvider> shapeGenerators;
  private int restarts = 0;
  
  public MapGenerator(Map<CellType, ShapeProvider> shapeGenerators) {
    this.shapeGenerators = shapeGenerators;
  }
  
  @Override
  public MarsMap generate(MapConfiguration configuration) {
    
    createEmptyMap(configuration.size());
    
    generateShapes(configuration);
    placeResources(configuration);
    
    log.logInfo(map.toString());
    // log.logInfo("restarts: " + restarts);
    return map;
  }
  
  private void generateShapes(MapConfiguration configuration) {
    List<ShapeBlueprint> shapeBlueprints = generateShapeConfigurations(
            configuration.ranges()).stream()
                                   .sorted(Comparator.comparingInt(ShapeBlueprint::size).reversed())
                                   .collect(Collectors.toList());
    
    shapeBlueprints.forEach(System.out::println);
    
    createShapes(shapeBlueprints, configuration.size());
  }
  
  private List<ShapeBlueprint> generateShapeConfigurations(Collection<RangeConfiguration> configuration) {
    List<ShapeBlueprint> shapeBlueprintList = new ArrayList<>();
    
    for (RangeConfiguration rangeConfig : configuration) {
      shapeBlueprintList.addAll(generateShapeSizes(rangeConfig.numberOfRanges(),
                                                   rangeConfig.numberOfElements(),
                                                   rangeConfig.type()));
    }
    return shapeBlueprintList;
  }
  
  private void createShapes(List<ShapeBlueprint> shapeBlueprintList, int size) {
    int attemptLimit = 100;
    
    shapeGenerationLoop:
    for (ShapeBlueprint shapeBlueprint : shapeBlueprintList) {
      int attemptsWithCurrentShapeSize = 0;
      ShapeProvider generator = shapeGenerators.get(shapeBlueprint.type());
      boolean isPlaced = false;
      while (!isPlaced) {
        Area generatedShape = generator.get(shapeBlueprint.size());
        
        attemptsWithCurrentShapeSize++;
        
        if (isSuccessfulShapePlacement(generatedShape, shapeBlueprint.type())) {
          isPlaced = true;
          attemptsWithCurrentShapeSize = 0;
        }
        
        // first version of exiting conditions - more thinking needed
        if (attemptsWithCurrentShapeSize >= attemptLimit) {
          if (restarts < attemptLimit) {
            restartGeneration(shapeBlueprintList, size);
          } else {
            createEmptyMap(size);
          }
          break shapeGenerationLoop;
        }
      }
    }
  }
  
  private void restartGeneration(List<ShapeBlueprint> shapes, int size) {
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
  
  private List<ShapeBlueprint> generateShapeSizes(int numberOfShapes, int numberOfTiles, CellType type) {
    int minimumShapeSizeFactor = 2;
    int minimumShapeSize = Math.max(numberOfTiles / (numberOfShapes * minimumShapeSizeFactor), 1);
    int remainingTilesToAssign = numberOfTiles;
    
    List<ShapeBlueprint> shapeBlueprints = new ArrayList<>();
    
    for (int i = 0; i < numberOfShapes - 1; i++) {
      int numberOfShapesToGenerate = numberOfShapes - 2 - i;
      int maximumShapeSize = remainingTilesToAssign - (numberOfShapesToGenerate * minimumShapeSize);
      int generatedSize = RANDOM.nextInt(minimumShapeSize, maximumShapeSize);
      int shapeSize = Math.min(generatedSize, maximumShapeSize - generatedSize);
      
      shapeBlueprints.add(new ShapeBlueprint(type, shapeSize));
      remainingTilesToAssign -= shapeSize;
    }
    shapeBlueprints.add(new ShapeBlueprint(type, remainingTilesToAssign));
    
    return shapeBlueprints;
  }
  
  private void createEmptyMap(int size) {
    this.map = new MarsMap(size);
  }
  
  private void placeResources(MapConfiguration mapConfiguration) {
    for (ResourceConfiguration configuration : mapConfiguration.resources()) {
      
      int numberOfResources = configuration.numberOfElements();
      CellType requiredNeighbor = configuration.neighbor();
      ArrayList<Coordinate> emptyCoordinates = getEmptyCells();
      
      while (numberOfResources > 0 && !emptyCoordinates.isEmpty()) {
        int emptyCoordinateIndex = RANDOM.nextInt(emptyCoordinates.size());
        Coordinate randomCoordinate = emptyCoordinates.get(emptyCoordinateIndex);
        emptyCoordinates.remove(emptyCoordinateIndex);
        if (isValidResourcePosition(randomCoordinate, requiredNeighbor, mapConfiguration.size())) {
          map.setCell(randomCoordinate, configuration.type());
          numberOfResources--;
        }
      }
      while (numberOfResources > 0) {
        Coordinate coordinate = new Coordinate(RANDOM.nextInt(map.getHeight()), RANDOM.nextInt(map.getWidth()));
        if (map.getCell(coordinate).getType() == CellType.EMPTY) {
          List<Cell> neighbours = map.getNeighbours(coordinate, 1);
          if (neighbours.stream().anyMatch(neighbour -> neighbour.getType() == configuration.type())) {
            map.setCell(coordinate, configuration.type());
            numberOfResources--;
          }
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
}
