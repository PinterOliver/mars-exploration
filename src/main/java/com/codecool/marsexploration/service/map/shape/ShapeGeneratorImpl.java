package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.map.Area;
import com.codecool.marsexploration.data.utilities.Coordinate;

import java.util.List;
import java.util.Random;

public abstract class ShapeGeneratorImpl implements ShapeGenerator {
  private final double defaultChance;
  private final double plusPerNeighbour;
  private final Random random;
  private final CellType cellType;
  private static final int QUANTITY_MULTIPLY = 3;
  private static final int SHAPE_SIDE_DIVIDE = 4;
  
  public ShapeGeneratorImpl(double defaultChance, double plusPerNeighbour, Random random, CellType cellType) {
    this.defaultChance = defaultChance;
    this.plusPerNeighbour = plusPerNeighbour;
    this.random = random;
    this.cellType = cellType;
  }
  
  @Override
  public Area generate(int quantity) {
    double averageShapeSide = Math.sqrt(quantity * QUANTITY_MULTIPLY); // side?
    double plusMinus = random.nextDouble(-averageShapeSide / SHAPE_SIDE_DIVIDE, averageShapeSide / SHAPE_SIDE_DIVIDE);
    int width = (int) (averageShapeSide + plusMinus);
    int height = (int) (averageShapeSide - plusMinus);
    
    Area generatedArea = new Area(height, width);
    
    placeCells(generatedArea, quantity);
    return generatedArea;
  }
  
  private void placeCells(Area area, int quantity) {
    int generatedCount = 0;
    int width = area.getWidth();
    int height = area.getHeight();
    
    while (quantity > generatedCount) {
      Coordinate randomCoordinate = new Coordinate(random.nextInt(height), random.nextInt(width));
      if (area.getCell(randomCoordinate).getType() == CellType.EMPTY) {
        List<Cell> neighbours = area.getNeighbours(randomCoordinate, 1);
        boolean generatedChanceResult = generateChanceResult(neighbours);
        if (generatedChanceResult) {
          area.setCell(randomCoordinate, cellType);
          generatedCount++;
        }
      }
    }
  }
  
  private boolean generateChanceResult(List<Cell> neighbours) {
    int neighbourCountWithSameType = (int) neighbours.stream().filter(e -> e.getType() == cellType).count();
    double chanceIncrease = neighbourCountWithSameType * plusPerNeighbour;
    double randomFromZeroToOne = random.nextDouble();
    return (chanceIncrease + defaultChance) > randomFromZeroToOne;
  }
}
