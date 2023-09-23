package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.map.ShapeGeneratorBlueprint;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ShapeGeneratorProvider {
  private static final double SPARSENESS_FACTOR = 0.1;
  private final Random random;
  Map<CellType, ShapeGeneratorBlueprint> blueprints;
  
  public ShapeGeneratorProvider(Random random, Map<CellType, ShapeGeneratorBlueprint> blueprints) {
    this.random = random;
    this.blueprints = new HashMap<>(blueprints);
  }
  
  public ShapeGenerator getShapeGenerator(CellType type, double sparsenessFactor) {
    return new GeneralShapeGenerator(sparsenessFactor, random, type);
  }
  
  public ShapeProvider getShapeGenerator(CellType type) {
    if (blueprints.containsKey(type)) {
      ShapeGeneratorBlueprint shapeData = blueprints.get(type);
      return new GeneralShapeGenerator(shapeData.sparsenessFactor(), random, type);
    } else {
      return new GeneralShapeGenerator(SPARSENESS_FACTOR, random, type);
    }
  }
}
