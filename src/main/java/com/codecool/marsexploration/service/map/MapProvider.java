package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.config.MapConfiguration;

public interface MapProvider {
  Cell[][] generate(MapConfiguration configuration);
}
