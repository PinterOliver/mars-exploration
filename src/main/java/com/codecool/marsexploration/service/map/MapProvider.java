package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.map.MarsMap;
import com.codecool.marsexploration.service.map.shape.ShapeGenerator;
import com.codecool.marsexploration.service.map.shape.ShapeProvider;

import java.util.Map;

public interface MapProvider {
  MarsMap generate(MapConfiguration configuration, Map<CellType, ShapeProvider> shapeGenerators);
}
