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
  
  public ShapeGeneratorImpl(double defaultChance, double plusPerNeighbour, Random random, CellType cellType) {
    this.defaultChance = defaultChance;
    this.plusPerNeighbour = plusPerNeighbour;
    this.random = random;
    this.cellType = cellType;
  }
  
  @Override
  public Area generate(int quantity) {
    double a = Math.sqrt(quantity * 3);
    double plusMinus = random.nextDouble(-a / 4, a / 4);
    int width = (int) (a + plusMinus);
    int height = (int) (a - plusMinus);
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
      // System.out.println("random coordinate: " + randomCoordinate);
      // System.out.println("width: " + width);
      // System.out.println("height: " + height);
      if (area.getCell(randomCoordinate).getType() == CellType.EMPTY) {
        List<Cell> neighbours = area.getNeighbours(randomCoordinate, 1);
        boolean generate = generateChance(neighbours);
        if (generate) {
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
