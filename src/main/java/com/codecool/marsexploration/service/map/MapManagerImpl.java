package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.map.MarsMap;
import com.codecool.marsexploration.service.filewriter.MapFileWriter;
import com.codecool.marsexploration.service.map.shape.ShapeGenerator;

import java.util.Map;

public class MapManagerImpl implements MapManager {
  private final MapProvider provider;
  private final MapFileWriter fileWriter;
  
  public MapManagerImpl(MapProvider provider, MapFileWriter fileWriter) {
    this.provider = provider;
    this.fileWriter = fileWriter;
  }
  
  @Override
  public void createMap(MapConfiguration configuration, String filePath, Map<CellType, ShapeGenerator> shapeGenerators) {
    MarsMap marsMap = provider.generate(configuration, shapeGenerators);
    String mapAsString = marsMap.toString();
    fileWriter.writeFile(filePath, mapAsString);
  }
}
