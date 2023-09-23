package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.service.map.shape.ShapeProvider;

import java.util.Map;

public interface MapManager {
  void createMap(MapConfiguration configuration, String filePath, Map<CellType, ShapeProvider> shapeGenerators);
}
