package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class PitShapeGenerator extends ShapeGenerator {
  public static final double SPARSENESS_FACTOR = 0.2;
  
  public PitShapeGenerator(Random random, CellType cellType) {
    super(SPARSENESS_FACTOR, random, cellType);
  }
}
