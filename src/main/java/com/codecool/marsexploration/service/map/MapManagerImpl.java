package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.Application;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.map.MarsMap;
import com.codecool.marsexploration.service.filewriter.MapFileWriter;

public class MapManagerImpl implements MapManager {
  private final MapProvider provider;
  private final MapFileWriter fileWriter;
  
  public MapManagerImpl(MapProvider provider, MapFileWriter fileWriter) {
    this.provider = provider;
    this.fileWriter = fileWriter;
  }
  
  @Override
  public void createMap(MapConfiguration configuration, String filePath) {
    MarsMap marsMap = provider.generate(configuration);
    String mapAsString = marsMap.toString();
    fileWriter.writeFile(filePath, mapAsString);
    fileWriter.writeFile(Application.FILE_PATH, mapAsString);
  }
}
