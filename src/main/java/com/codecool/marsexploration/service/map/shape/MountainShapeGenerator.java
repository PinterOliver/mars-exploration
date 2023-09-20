package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class MountainShapeGenerator extends ShapeGeneratorImpl {
  private static final double DEFAULT_CHANCE = 0.1;
  private static final double PLUS_PER_NEIGHBOUR = 0.2;
  private static final CellType CELL_TYPE = CellType.MOUNTAIN;
  
  public MountainShapeGenerator(Random random) {
    super(DEFAULT_CHANCE, PLUS_PER_NEIGHBOUR, random, CELL_TYPE);
  }
}
