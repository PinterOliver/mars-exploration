package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class LakeShapeGenerator extends ShapeGenerator {
  private static final double DEFAULT_CHANCE = 0;
  private static final double PLUS_PER_NEIGHBOR = 0.125;
  
  public LakeShapeGenerator(Random random, CellType cellType) {
    super(DEFAULT_CHANCE, PLUS_PER_NEIGHBOR, random, cellType);
  }
}
