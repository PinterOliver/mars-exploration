package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.ClusterConfiguration;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.ResourceConfiguration;
import com.codecool.marsexploration.data.map.Area;
import com.codecool.marsexploration.data.map.MarsMap;
import com.codecool.marsexploration.data.map.ShapeBlueprint;
import com.codecool.marsexploration.data.utilities.Coordinate;
import com.codecool.marsexploration.service.map.shape.ShapeProvider;
import com.codecool.marsexploration.service.utilities.Pick;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MapGenerator implements MapProvider {
  private final Random random;
  private final Pick pick;
  private final Map<CellType, ShapeProvider> shapeGenerators;
  private MarsMap map;
  private int restarts = 0;
  
  public MapGenerator(Map<CellType, ShapeProvider> shapeGenerators, Random random, Pick pick) {
    this.shapeGenerators = new HashMap<>(shapeGenerators);
    this.random = random;
    this.pick = pick;
  }
  
  @Override
  public MarsMap generate(MapConfiguration configuration) {
    createEmptyMap(configuration.size());
    
    generateShapes(configuration);
    placeResources(configuration);
    placeAlien();
    
    return map;
  }
  
  private void placeAlien() {
    Collection<Coordinate> emptyCoordinates = new ArrayList<>(map.filterCellsByType(CellType.EMPTY));
    
    Optional<Coordinate> randomCoordinate = pick.from(emptyCoordinates);
    randomCoordinate.ifPresent(coordinate -> map.setCell(coordinate, CellType.ALIEN));
  }
  
  private void generateShapes(MapConfiguration configuration) {
    List<ShapeBlueprint> shapeBlueprints = generateShapeConfigurations(configuration.clusters()).stream()
                                                                                                .sorted(Comparator.comparingInt(
                                                                                                                          ShapeBlueprint::size)
                                                                                                                  .reversed())
                                                                                                .collect(Collectors.toList());
    
    createShapes(shapeBlueprints, configuration.size());
  }
  
  private List<ShapeBlueprint> generateShapeConfigurations(Collection<ClusterConfiguration> configuration) {
    List<ShapeBlueprint> shapeBlueprintList = new ArrayList<>();
    
    for (ClusterConfiguration clusterConfiguration : configuration) {
      shapeBlueprintList.addAll(generateShapeSizes(clusterConfiguration.numberOfClusters(),
                                                   clusterConfiguration.numberOfElements(),
                                                   clusterConfiguration.clusterType()));
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
        
        if (manageShapePlacement(generatedShape, shapeBlueprint.type())) {
          isPlaced = true;
          attemptsWithCurrentShapeSize = 0;
        }
        
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
      int startPointIndex = random.nextInt(possibleStartPoints.size());
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
    int indexOffset = 1;
    
    List<ShapeBlueprint> shapeBlueprints = new ArrayList<>();
    
    for (int i = 1; i < numberOfShapes; i++) {
      int numberOfShapesToGenerate = numberOfShapes - indexOffset - i;
      int maximumShapeSize = remainingTilesToAssign - (numberOfShapesToGenerate * minimumShapeSize);
      int generatedSize =
              (int) Math.pow(random.nextDouble(Math.sqrt(minimumShapeSize), Math.sqrt(maximumShapeSize)), 2);
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
    for (ClusterConfiguration configuration : mapConfiguration.clusters()) {
      int numberOfResources =
              configuration.resourceTypes().stream().mapToInt(ResourceConfiguration::numberOfElements).sum();
      CellType requiredNeighbor = configuration.clusterType();
      ArrayList<Coordinate> emptyCoordinates = new ArrayList<>(map.filterCellsByType(CellType.EMPTY));
      
      numberOfResources = placeResourcePlaceholdersNextToCluster(numberOfResources, emptyCoordinates, requiredNeighbor);
      placeResourcePlaceholdersNextToResourcePlaceHolders(numberOfResources);
      
      replacePlaceHolderWithResourceCells(configuration);
    }
  }
  
  private int placeResourcePlaceholdersNextToCluster(int numberOfResources, ArrayList<Coordinate> emptyCoordinates,
          CellType requiredNeighbor) {
    while (numberOfResources > 0 && !emptyCoordinates.isEmpty()) {
      Optional<Coordinate> optionalCoordinate = pick.from(emptyCoordinates);
      if (optionalCoordinate.isPresent()) {
        Coordinate coordinate = optionalCoordinate.get();
        emptyCoordinates.remove(coordinate);
        if (isValidResourcePosition(coordinate, requiredNeighbor)) {
          map.setCell(coordinate, CellType.PLACEHOLDER);
          numberOfResources--;
        }
      }
    }
    return numberOfResources;
  }
  
  private void placeResourcePlaceholdersNextToResourcePlaceHolders(int numberOfResources) {
    while (numberOfResources > 0) {
      Coordinate coordinate = new Coordinate(random.nextInt(map.getHeight()), random.nextInt(map.getWidth()));
      if (map.getCell(coordinate).getType() == CellType.EMPTY) {
        if (isValidResourcePosition(coordinate, CellType.PLACEHOLDER)) {
          map.setCell(coordinate, CellType.PLACEHOLDER);
          numberOfResources--;
        }
      }
    }
  }
  
  private void replacePlaceHolderWithResourceCells(@NotNull ClusterConfiguration configuration) {
    Collection<Coordinate> placeHolders = new HashSet<>(map.filterCellsByType(CellType.PLACEHOLDER));
    for (ResourceConfiguration resourceConfiguration : configuration.resourceTypes()) {
      for (int i = 0; i < resourceConfiguration.numberOfElements(); i++) {
        Optional<Coordinate> cell = pick.from(placeHolders);
        if (cell.isPresent()) {
          map.setCell(cell.get(), resourceConfiguration.resourceType());
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
