package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Random;

public class GeneralShapeGenerator extends ShapeGenerator{
  public GeneralShapeGenerator(double defaultChance, double plusPerNeighbour, Random random, CellType cellType) {
    super(defaultChance, plusPerNeighbour, random, cellType);
  }
}
