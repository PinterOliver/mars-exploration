package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class GeneralShapeGenerator extends ShapeGenerator implements ShapeProvider{
  
  public GeneralShapeGenerator(double defaultChance, Random random, CellType cellType) {
    super(defaultChance, 0.1, random, cellType);
  }
}
