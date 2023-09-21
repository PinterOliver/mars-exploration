package com.codecool.marsexploration.service.validation;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import com.codecool.marsexploration.data.config.RangeWithNumbersConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class MapConfigurationValidatorImplTest {
  RangeWithResource mountainResources = new RangeWithResource(CellType.MOUNTAIN, Set.of(CellType.MINERAL));
  RangeWithResource pitResources = new RangeWithResource(CellType.PIT, Set.of(CellType.WATER));
  MapConfigurationValidator validator = new MapConfigurationValidatorImpl();
  
  @Test
  void isValidTestWithFewSize() {
    MapValidationConfiguration validationConfiguration = new MapValidationConfiguration(0.8, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    MapConfiguration mapConfiguration = getConfiguration(9, 10, 2, 10, 2, 5, 5);
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertFalse(validatorResponse);
  }
  
  @Test
  void isValidTestWithTooManySize(){
    MapConfiguration mapConfiguration = getConfiguration(31, 80, 3, 80, 3, 10, 10);
    MapValidationConfiguration validationConfiguration = new MapValidationConfiguration(0.8, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertFalse(validatorResponse);
  }
  
  @Test
  void isValidTestWithLimitValue(){
    MapConfiguration mapConfiguration = getConfiguration(10, 23, 1, 5, 1, 1, 1);
    MapValidationConfiguration validationConfiguration = new MapValidationConfiguration(0.3, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertTrue(validatorResponse);
  }
  
  @Test
  void isValidTestWithOverTheLimit(){
    MapConfiguration mapConfiguration = getConfiguration(10, 23, 1, 5, 1, 1, 1);
    MapValidationConfiguration validationConfiguration = new MapValidationConfiguration(0.29, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertFalse(validatorResponse);
  }
  
  @Test
  void isValidTestWithNullConfiguration(){
    MapConfiguration mapConfiguration = null;
    MapValidationConfiguration validationConfiguration = new MapValidationConfiguration(0.29, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertFalse(validatorResponse);
  }
  
  @NotNull
  private static MapConfiguration getConfiguration(int size, int numberOfMountain, int mountainRanges, int numberOfPit, int pitRanges, int mineralAmount, int waterAmount) {
    ResourceConfiguration mineralResourceConfiguration = new ResourceConfiguration(CellType.MINERAL, mineralAmount);
    ResourceConfiguration waterResourceConfiguration = new ResourceConfiguration(CellType.WATER, waterAmount);
    RangeWithNumbersConfiguration mountainRangeConfiguration = new RangeWithNumbersConfiguration(CellType.MOUNTAIN, numberOfMountain, mountainRanges, Set.of(mineralResourceConfiguration));
    RangeWithNumbersConfiguration pitRangeConfiguration = new RangeWithNumbersConfiguration(CellType.PIT, numberOfPit, pitRanges, Set.of(waterResourceConfiguration));
    
    return new MapConfiguration(size, List.of(mountainRangeConfiguration, pitRangeConfiguration));
  }
}