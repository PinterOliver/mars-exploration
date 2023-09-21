package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class MountainShapeGenerator extends ShapeGenerator {
  private static final double DEFAULT_CHANCE = 0.1;
  private static final double PLUS_PER_NEIGHBOUR = 0.1;
  
  public MountainShapeGenerator(Random random, CellType cellType) {
    super(DEFAULT_CHANCE, PLUS_PER_NEIGHBOUR, random, cellType);
  }
}
