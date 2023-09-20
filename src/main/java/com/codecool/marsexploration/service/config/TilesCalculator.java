package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.data.utilities.Interval;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class TilesCalculator implements TilesManager {
  private int remainingFreeTiles;
  private int minimumRangeNumber;
  private int minimumResourceNumber;
  private Collection<CellType> rangeTypes;
  private Collection<CellType> resourceTypes;
  
  @Override
  public void startManagingTiles(int size, @NotNull MapValidationConfiguration validationConfiguration,
          Collection<CellType> rangeTypes, Collection<CellType> resourceTypes) {
    int mapSize = (int) Math.pow(size, 2);
    
    remainingFreeTiles = (int) (mapSize * validationConfiguration.maxFilledTilesRatio());
    minimumRangeNumber = (int) (mapSize * validationConfiguration.minimumRangeTypeRatio());
    minimumResourceNumber = (int) (mapSize * validationConfiguration.minimumResourceTypeRatio());
    
    this.rangeTypes = new HashSet<>(rangeTypes);
    this.resourceTypes = new HashSet<>(resourceTypes);
  }
  
  @Override
  public int getRemainingFreeTiles() {
    return remainingFreeTiles;
  }
  
  @Override
  public Interval<Integer> getTypeElementInterval(CellType type) {
    if (rangeTypes.contains(type)) {
      return getRangeInterval();
    }
    if (resourceTypes.contains(type)) {
      return getResourceInterval();
    }
    return null;
  }
  
  @Override
  public boolean remove(CellType type, int numberOfElements) {
    if (rangeTypes.remove(type) || resourceTypes.remove(type)) {
      remainingFreeTiles -= numberOfElements;
      return true;
    }
    return false;
  }
  
  private @NotNull Interval<Integer> getRangeInterval() {
    return new Interval<>(minimumRangeNumber, getMaximumRangeNumber());
  }
  
  private @NotNull Interval<Integer> getResourceInterval() {
    return new Interval<>(minimumResourceNumber, getMaximumResourceNumber());
  }
  
  private int getMaximumRangeNumber() {
    return remainingFreeTiles - (rangeTypes.size() - 1) * minimumRangeNumber -
           resourceTypes.size() * minimumResourceNumber;
  }
  
  private int getMaximumResourceNumber() {
    return remainingFreeTiles - (rangeTypes.size()) * minimumRangeNumber -
           (resourceTypes.size() - 1) * minimumResourceNumber;
  }
}
