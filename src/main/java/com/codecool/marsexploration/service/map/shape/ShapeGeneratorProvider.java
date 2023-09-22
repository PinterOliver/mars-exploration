package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.map.ShapeGeneratorBlueprint;

import java.util.Map;
import java.util.Random;

public class ShapeGeneratorProvider {
  private final Random RANDOM;
  private static final double DEFAULT_CHANCE = 0.1;
  private static final double PLUS_PER_NEIGHBOR = 0.1;
  Map<CellType, ShapeGeneratorBlueprint> blueprints;
  public ShapeGeneratorProvider(Random random, Map<CellType, ShapeGeneratorBlueprint> blueprints) {
    this.RANDOM = random;
    this.blueprints = blueprints;
  }
  public ShapeGenerator getShapeGenerator(CellType type, double defaultChance){
    return new GeneralShapeGenerator(defaultChance, RANDOM, type);
  }
  
  public ShapeProvider getShapeGenerator(CellType type){
    if (blueprints.containsKey(type)){
      ShapeGeneratorBlueprint shapeData = blueprints.get(type);
      return new GeneralShapeGenerator(shapeData.defaultChance(), RANDOM, type);
    } else {
      return new GeneralShapeGenerator(DEFAULT_CHANCE, RANDOM, type);
    }
  }
}
