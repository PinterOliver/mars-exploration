package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class MountainShapeGenerator extends ShapeGenerator {
  private static final double SPARSENESS_FACTOR = 0.12;
  
  public MountainShapeGenerator(Random random, CellType cellType) {
    super(SPARSENESS_FACTOR, random, cellType);
  }
}
