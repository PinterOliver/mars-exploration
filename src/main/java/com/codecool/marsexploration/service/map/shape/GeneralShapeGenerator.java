package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class GeneralShapeGenerator extends ShapeGenerator implements ShapeProvider {
  public GeneralShapeGenerator(double sparsenessFactor, Random random, CellType cellType) {
    super(sparsenessFactor, random, cellType);
  }
}
