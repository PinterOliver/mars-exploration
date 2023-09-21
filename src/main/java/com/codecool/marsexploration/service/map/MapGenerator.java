package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.RangeWithNumbersConfiguration;
import com.codecool.marsexploration.data.config.ResourceConfiguration;
import com.codecool.marsexploration.data.map.Area;
import com.codecool.marsexploration.data.map.MarsMap;
import com.codecool.marsexploration.data.map.ShapeBlueprint;
import com.codecool.marsexploration.data.utilities.Coordinate;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.map.shape.ShapeProvider;
import com.codecool.marsexploration.service.utilities.Pick;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MapGenerator implements MapProvider {
  private final Random RANDOM;
  private final Pick PICK;
  private final Logger log;
  private MarsMap map;
  private final Map<CellType, ShapeProvider> shapeGenerators;
  private int restarts = 0;
  
  public MapGenerator(Map<CellType, ShapeProvider> shapeGenerators, Random RANDOM, Pick PICK, Logger log) {
    this.shapeGenerators = new HashMap<>(shapeGenerators);
    this.RANDOM = RANDOM;
    this.PICK = PICK;
    this.log = log;
  }
  
  @Override
  public MarsMap generate(MapConfiguration configuration) {
    
    createEmptyMap(configuration.size());
    
    generateShapes(configuration);
    placeResources(configuration);
    placeAlien();
    
    // log.logInfo(map.toString());
    // log.logInfo("restarts: " + restarts);
    return map;
  }
  
  private void placeAlien() {
    Collection<Coordinate> emptyCoordinates = new ArrayList<>(map.filterCellsByType(CellType.EMPTY));
    
    Optional<Coordinate> randomCoordinate = PICK.from(emptyCoordinates);
    randomCoordinate.ifPresent(coordinate -> map.setCell(coordinate, CellType.ALIEN));
  }
  
  private void generateShapes(MapConfiguration configuration) {
    List<ShapeBlueprint> shapeBlueprints = generateShapeConfigurations(configuration.ranges()).stream()
                                                                                              .sorted(Comparator.comparingInt(
                                                                                                                        ShapeBlueprint::size)
                                                                                                                .reversed())
                                                                                              .collect(Collectors.toList());
    
    shapeBlueprints.forEach(System.out::println);
    
    createShapes(shapeBlueprints, configuration.size());
  }
  
  private List<ShapeBlueprint> generateShapeConfigurations(Collection<RangeWithNumbersConfiguration> configuration) {
    List<ShapeBlueprint> shapeBlueprintList = new ArrayList<>();
    
    for (RangeWithNumbersConfiguration rangeConfig : configuration) {
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
        // attemptsWithCurrentShapeSize++;
        
        if (manageShapePlacement(generatedShape, shapeBlueprint.type())) {
          isPlaced = true;
          attemptsWithCurrentShapeSize = 0;
        }
        
        // first version of exiting conditions - more thinking needed
        if (++attemptsWithCurrentShapeSize > attemptLimit) {
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
  
  private boolean manageShapePlacement(Area generatedShape, CellType type) {
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
        Coordinate cuurentCoordinate = new Coordinate(row, column);
        if (generatedShape.getCell(cuurentCoordinate).getType() == type) {
          map.setCell(startPoint.add(cuurentCoordinate), type);
        }
      }
    }
  }
  
  private boolean validateShapePlacement(Coordinate startPoint, Area generatedShape) {
    for (int row = 0; row < generatedShape.getHeight(); row++) {
      for (int column = 0; column < generatedShape.getWidth(); column++) {
        Coordinate currentCoordinate = new Coordinate(row, column);
        if (!(generatedShape.getCell(currentCoordinate).getType() == CellType.EMPTY) &&
          !(map.getCell(startPoint.add(currentCoordinate)).getType() == CellType.EMPTY)) {
            return false;
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
  
  private void placeResources(@NotNull MapConfiguration mapConfiguration) {
    for (RangeWithNumbersConfiguration configuration : mapConfiguration.ranges()) {

      int numberOfResources =
              configuration.resourceTypes().stream().mapToInt(ResourceConfiguration::numberOfElements).sum();
      CellType requiredNeighbor = configuration.type();
      ArrayList<Coordinate> emptyCoordinates = new ArrayList<>(map.filterCellsByType(CellType.EMPTY));
      // int numberOfResources = configuration.numberOfElements();
      // CellType requiredNeighbor = configuration.neighbor();
      // Collection<Coordinate> emptyCoordinates = new ArrayList<>(map.filterCellsByType(CellType.EMPTY));
      
      numberOfResources =
              placePlaceHoldersNextToRanges(mapConfiguration, numberOfResources, emptyCoordinates, requiredNeighbor);
      placePlaceHoldersNextToPlaceHolders(numberOfResources);
      
      replacePlaceHolderWithResourceCells(configuration);
    }
  }
  
  private int placePlaceHoldersNextToRanges(@NotNull MapConfiguration mapConfiguration, int numberOfResources,
          ArrayList<Coordinate> emptyCoordinates, CellType requiredNeighbor) {
    while (numberOfResources > 0 && !emptyCoordinates.isEmpty()) {
      int emptyCoordinateIndex = RANDOM.nextInt(emptyCoordinates.size());
      Coordinate randomCoordinate = emptyCoordinates.get(emptyCoordinateIndex);
      emptyCoordinates.remove(emptyCoordinateIndex);
      if (isValidResourcePosition(randomCoordinate, requiredNeighbor)) {
        map.setCell(randomCoordinate, CellType.PLACEHOLDER);
        numberOfResources--;
      }
    }
    return numberOfResources;
  }
  
  private void placePlaceHoldersNextToPlaceHolders(int numberOfResources) {
    while (numberOfResources > 0) {
      Coordinate coordinate = new Coordinate(RANDOM.nextInt(map.getHeight()), RANDOM.nextInt(map.getWidth()));
      if (map.getCell(coordinate).getType() == CellType.EMPTY) {
        List<Cell> neighbours = map.getNeighbours(coordinate, 1);
        if (neighbours.stream().anyMatch(neighbour -> neighbour.getType() == CellType.PLACEHOLDER)) {
          map.setCell(coordinate, CellType.PLACEHOLDER);
          numberOfResources--;
        }
      }
    }
  }
  
  private void replacePlaceHolderWithResourceCells(@NotNull RangeWithNumbersConfiguration configuration) {
    Collection<Coordinate> placeHolders = new HashSet<>(map.filterCellsByType(CellType.PLACEHOLDER));
    for (ResourceConfiguration resourceConfiguration : configuration.resourceTypes()) {
      for (int i = 0; i < resourceConfiguration.numberOfElements(); i++) {
        Optional<Coordinate> cell = PICK.from(placeHolders);
        if (cell.isPresent()) {
          map.setCell(cell.get(), resourceConfiguration.type());
          placeHolders.remove(cell.get());
        }
      }
    }
  }
  
  private boolean isValidResourcePosition(Coordinate randomCoordinate, CellType requiredNeighbor) {
    Collection<Cell> neighborCells = map.getNeighbours(randomCoordinate, 1);
    return neighborCells.stream().anyMatch(cell -> cell.getType() == requiredNeighbor);
  }
}
