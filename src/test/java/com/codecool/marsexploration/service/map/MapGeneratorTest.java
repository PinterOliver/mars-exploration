package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.ClusterConfiguration;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.ResourceConfiguration;
import com.codecool.marsexploration.data.map.MarsMap;
import com.codecool.marsexploration.service.map.shape.MountainShapeGenerator;
import com.codecool.marsexploration.service.map.shape.PitShapeGenerator;
import com.codecool.marsexploration.service.map.shape.ShapeProvider;
import com.codecool.marsexploration.service.utilities.Pick;
import com.codecool.marsexploration.service.utilities.PickImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MapGeneratorTest {
  private final Random random = new Random();
  private final Pick pick = new PickImpl(random);
  private final Map<CellType, ShapeProvider> shapeGenerators = Map.of(CellType.MOUNTAIN,
                                                                      new MountainShapeGenerator(random,
                                                                                                 CellType.MOUNTAIN),
                                                                      CellType.PIT,
                                                                      new PitShapeGenerator(random, CellType.PIT));
  
  @Test
  public void generateTestWithEnoughPlaceForWater() {
    MapProvider provider = new MapGenerator(shapeGenerators, random, pick);
    int waterElementCount = 10;
    int pitElementCount = 40;
    int pitRangeCount = 6;
    int mapSize = 10;
    ResourceConfiguration waterConfiguration = new ResourceConfiguration(CellType.WATER, waterElementCount);
    ClusterConfiguration pitConfiguration =
            new ClusterConfiguration(CellType.PIT, pitElementCount, pitRangeCount, Set.of(waterConfiguration));
    MapConfiguration configuration = new MapConfiguration(mapSize, List.of(pitConfiguration));
    MarsMap map = provider.generate(configuration);
    
    System.out.println(map);
    assertTrue(checkElementCount("~", waterElementCount, map));
    assertTrue(checkElementCount("#", pitElementCount, map));
    assertTrue(checkLengthAndWith(map, mapSize));
  }
  
  @Test
  public void generateTestWithoutEnoughPlaceForWater() {
    MapProvider provider = new MapGenerator(shapeGenerators, random, pick);
    int waterElementCount = 60;
    int pitElementCount = 60;
    int pitRangeCount = 1;
    int mapSize = 20;
    ResourceConfiguration waterConfiguration = new ResourceConfiguration(CellType.WATER, waterElementCount);
    ClusterConfiguration pitConfiguration =
            new ClusterConfiguration(CellType.PIT, pitElementCount, pitRangeCount, Set.of(waterConfiguration));
    MapConfiguration configuration = new MapConfiguration(mapSize, List.of(pitConfiguration));
    MarsMap map = provider.generate(configuration);
    
    System.out.println(map);
    assertTrue(checkElementCount("~", waterElementCount, map));
    assertTrue(checkElementCount("#", pitElementCount, map));
    assertTrue(checkLengthAndWith(map, mapSize));
  }
  
  private boolean checkElementCount(String c, int expected, MarsMap map) {
    int amount = map.toString().length() - map.toString().replace(c, "").length();
    return expected == amount;
  }
  
  private boolean checkLengthAndWith(MarsMap map, int expected) {
    String[] rows = map.toString().split("\n");
    return rows.length - 1 == expected && rows[1].length() == expected;
  }
}
