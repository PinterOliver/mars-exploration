package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class LakeShapeGenerator extends ShapeGenerator {
  private static final double SPARSENESS_FACTOR = 0;
  
  public LakeShapeGenerator(Random random, CellType cellType) {
    super(SPARSENESS_FACTOR, random, cellType);
  }
}
