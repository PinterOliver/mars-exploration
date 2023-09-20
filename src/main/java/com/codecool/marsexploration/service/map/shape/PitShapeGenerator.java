package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class PitShapeGenerator extends ShapeGeneratorImpl {
  public static final double DEFAULT_CHANCE = 0.1;
  public static final double PLUS_PER_NEIGHBOUR = 0.1;
  private static final CellType CELL_TYPE = CellType.PIT;
  
  public PitShapeGenerator(Random random) {
    super(DEFAULT_CHANCE, PLUS_PER_NEIGHBOUR, random, CELL_TYPE);
  }
}
