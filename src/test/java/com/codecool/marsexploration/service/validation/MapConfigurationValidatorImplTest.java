package com.codecool.marsexploration.service.validation;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapConfigurationValidatorImplTest {
  private final Cluster mountainResources = new Cluster(CellType.MOUNTAIN, Set.of(CellType.MINERAL));
  private final Cluster pitResources = new Cluster(CellType.PIT, Set.of(CellType.WATER));
  private final MapConfigurationValidator validator = new MapConfigurationValidatorImpl();
  
  @NotNull
  private static MapConfiguration getConfiguration(int size, int numberOfMountain, int mountainRanges, int numberOfPit,
          int pitRanges, int mineralAmount, int waterAmount) {
    ResourceConfiguration mineralResourceConfiguration = new ResourceConfiguration(CellType.MINERAL, mineralAmount);
    ResourceConfiguration waterResourceConfiguration = new ResourceConfiguration(CellType.WATER, waterAmount);
    ClusterConfiguration mountainRangeConfiguration = new ClusterConfiguration(CellType.MOUNTAIN,
                                                                               numberOfMountain,
                                                                               mountainRanges,
                                                                               Set.of(mineralResourceConfiguration));
    ClusterConfiguration pitRangeConfiguration =
            new ClusterConfiguration(CellType.PIT, numberOfPit, pitRanges, Set.of(waterResourceConfiguration));
    
    return new MapConfiguration(size, List.of(mountainRangeConfiguration, pitRangeConfiguration));
  }
  
  @Test
  public void isValidTestWithFewSize() {
    MapValidationConfiguration validationConfiguration =
            new MapValidationConfiguration(0.8, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    MapConfiguration mapConfiguration = getConfiguration(9, 10, 2, 10, 2, 5, 5);
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertFalse(validatorResponse);
  }
  
  @Test
  public void isValidTestWithTooManySize() {
    MapConfiguration mapConfiguration = getConfiguration(31, 80, 3, 80, 3, 10, 10);
    MapValidationConfiguration validationConfiguration =
            new MapValidationConfiguration(0.8, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertFalse(validatorResponse);
  }
  
  @Test
  public void isValidTestWithLimitValue() {
    MapConfiguration mapConfiguration = getConfiguration(10, 23, 1, 5, 1, 1, 1);
    MapValidationConfiguration validationConfiguration =
            new MapValidationConfiguration(0.3, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertTrue(validatorResponse);
  }
  
  @Test
  public void isValidTestWithOverTheLimit() {
    MapConfiguration mapConfiguration = getConfiguration(10, 23, 1, 5, 1, 1, 1);
    MapValidationConfiguration validationConfiguration =
            new MapValidationConfiguration(0.29, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    
    boolean validatorResponse = validator.isValid(mapConfiguration, validationConfiguration);
    
    assertFalse(validatorResponse);
  }
  
  @Test
  public void isValidTestWithNullConfiguration() {
    MapValidationConfiguration validationConfiguration =
            new MapValidationConfiguration(0.29, 10, 30, 0.05, 0.01, List.of(mountainResources, pitResources));
    
    boolean validatorResponse = validator.isValid(null, validationConfiguration);
    
    assertFalse(validatorResponse);
  }
}
