package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.map.Area;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShapeGeneratorTest {
  private final Random random = new Random();
  
  @Test
  public void mountainGeneratorGet() {
    ShapeProvider provider = new MountainShapeGenerator(random, CellType.MOUNTAIN);
    int quantityExpected = 10;
    Area area = provider.get(quantityExpected);
    int mountainCountActual = area.toString().length() - area.toString().replace("^", "").length();
    assertEquals(quantityExpected, mountainCountActual);
  }
  
  @Test
  public void pitGeneratorGetTest() {
    ShapeProvider provider = new PitShapeGenerator(random, CellType.PIT);
    int quantityExpected = 10;
    Area area = provider.get(quantityExpected);
    int mountainCountActual = area.toString().length() - area.toString().replace("#", "").length();
    assertEquals(quantityExpected, mountainCountActual);
  }
}
