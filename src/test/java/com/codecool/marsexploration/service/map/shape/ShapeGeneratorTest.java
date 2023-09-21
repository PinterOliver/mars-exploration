package com.codecool.marsexploration.service.map.shape;

import com.codecool.marsexploration.data.map.Area;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShapeGeneratorTest {
  Random random = new Random();
  @Test
  void mountainGeneratorGet() {
    ShapeProvider provider = new MountainShapeGenerator(random);
    int quantityExpected = 10;
    Area area = provider.get(quantityExpected);
    int mountainCountActual = area.toString().length() - area.toString().replace("^", "").length();
    assertEquals(quantityExpected, mountainCountActual);
  }
  
  @Test
  void pitGeneratorGetTest(){
    ShapeProvider provider = new PitShapeGenerator(random);
    int quantityExpected = 10;
    Area area = provider.get(quantityExpected);
    int mountainCountActual = area.toString().length() - area.toString().replace("#", "").length();
    assertEquals(quantityExpected, mountainCountActual);
  }
}