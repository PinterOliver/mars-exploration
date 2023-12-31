package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.map.Area;
import com.codecool.marsexploration.data.utilities.Coordinate;

import java.util.List;
import java.util.Random;

public abstract class ShapeGenerator implements ShapeProvider {
  public static final int NUMBER_OF_NEIGHBORS = 8;
  private static final int QUANTITY_MULTIPLY = 3;
  private static final int SHAPE_SIDE_DIVIDE = 4;
  private final double defaultChance;
  private final double plusPerNeighbour;
  private final Random random;
  private final CellType cellType;
  
  public ShapeGenerator(double sparsenessFactor, Random random, CellType cellType) {
    this.defaultChance = sparsenessFactor;
    this.plusPerNeighbour = (1 - sparsenessFactor) / NUMBER_OF_NEIGHBORS;
    this.random = random;
    this.cellType = cellType;
  }
  
  @Override
  public Area get(int quantity) {
    double averageShapeSide = Math.sqrt(quantity * QUANTITY_MULTIPLY);
    double plusMinus = random.nextDouble(-averageShapeSide / SHAPE_SIDE_DIVIDE, averageShapeSide / SHAPE_SIDE_DIVIDE);
    int width = (int) (averageShapeSide + plusMinus);
    int height = (int) (averageShapeSide - plusMinus);
    Area generatedArea = new Area(height, width);
    
    placeCells(generatedArea, quantity);
    return generatedArea;
  }
  
  private void placeCells(Area area, int quantity) {
    int width = area.getWidth();
    int height = area.getHeight();
    
    area.setCell(new Coordinate(height / 2, width / 2), cellType);
    int generatedCount = 1;
    
    while (quantity > generatedCount) {
      Coordinate randomCoordinate = new Coordinate(random.nextInt(height), random.nextInt(width));
      if (area.getCell(randomCoordinate).getType() == CellType.EMPTY) {
        List<Cell> neighbours = area.getNeighbours(randomCoordinate, 1);
        boolean generatedChanceResult = generateChance(neighbours);
        if (generatedChanceResult) {
          area.setCell(randomCoordinate, cellType);
          generatedCount++;
        }
      }
    }
  }
  
  private boolean generateChance(List<Cell> neighbours) {
    int neighbourWithSameType = (int) neighbours.stream().filter(e -> e.getType() == cellType).count();
    double chanceIncrease = neighbourWithSameType * plusPerNeighbour;
    double randomFromZeroToOne = random.nextDouble();
    return (chanceIncrease + defaultChance) > randomFromZeroToOne;
  }
}
