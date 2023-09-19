package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.map.MarsMap;

public interface MapProvider {
  MarsMap generate(MapConfiguration configuration);
}
