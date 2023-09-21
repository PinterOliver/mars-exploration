package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.map.ShapeGeneratorBlueprint;

import java.util.Map;
import java.util.Random;

public class ShapeGeneratorProvider {
  private final Random RANDOM;
  private static final double DEFAULT_CHANCE = 0.1;
  private static final double PLUS_PER_NEIGHBOR = 0.1;
  Map<CellType, ShapeGeneratorBlueprint> BLUEPRINTS = Map.of(
          CellType.MOUNTAIN, new ShapeGeneratorBlueprint(CellType.MOUNTAIN,0.1, 0.1),
          CellType.PIT, new ShapeGeneratorBlueprint(CellType.PIT,0.1, 0.05),
          CellType.WATER, new ShapeGeneratorBlueprint(CellType.WATER,0, 0.125));
  public ShapeGeneratorProvider(Random random) {
    this.RANDOM = random;
  }
  public ShapeGenerator getShapeGenerator(CellType type, double defaultChance, double plusPerNeighbour){
    return new GeneralShapeGenerator(defaultChance, plusPerNeighbour, RANDOM, type);
  }
  
  public ShapeGenerator getShapeGenerator(CellType type){
    if (BLUEPRINTS.containsKey(type)){
      ShapeGeneratorBlueprint shapeData = BLUEPRINTS.get(type);
      return new GeneralShapeGenerator(shapeData.defaultChance(), shapeData.plusPerNeighbour(), RANDOM, type);
    } else {
      return new GeneralShapeGenerator(DEFAULT_CHANCE, PLUS_PER_NEIGHBOR, RANDOM, type);
    }
  }
}
