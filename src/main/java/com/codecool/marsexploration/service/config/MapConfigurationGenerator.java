package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapConfiguration;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.service.validation.MapConfigurationValidator;
import com.codecool.marsexploration.service.validation.MapConfigurationValidatorImpl;

import java.util.List;

public class MapConfigurationGenerator {
  final static MapConfigurationValidator validator = new MapConfigurationValidatorImpl(new MapValidationConfiguration(0.3,
                                                                                                                      10,
                                                                                                                      40,
                                                                                                                      0.05,
                                                                                                                      0.01,
                                                                                                                      List.of(CellType.MOUNTAIN,
                                                                                                                              CellType.PIT),
                                                                                                                      List.of(CellType.WATER,
                                                                                                                              CellType.MINERAL)));
  
  public MapConfiguration get(){
    
    return null;
  }
}
