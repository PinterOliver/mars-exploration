package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.data.config.RangeWithResource;
import com.codecool.marsexploration.data.utilities.Interval;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class TilesCalculator implements TilesManager {
  private int remainingFreeTiles;
  private int minimumRangeNumber;
  private int minimumResourceNumber;
  private Collection<CellType> rangeTypes;
  private Collection<CellType> resourceTypes;
  
  @Override
  public void startManagingTiles(int size, @NotNull MapValidationConfiguration validationConfiguration) {
    int mapSize = (int) Math.pow(size, 2);
    
    setRangeTypes(validationConfiguration);
    setResourceTypes(validationConfiguration);
    
    remainingFreeTiles = (int) (mapSize * validationConfiguration.maxFilledTilesRatio());
    minimumRangeNumber = (int) (mapSize * validationConfiguration.minimumRangeTypeRatio());
    minimumResourceNumber = (int) (mapSize * validationConfiguration.minimumResourceTypeRatio());
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
  
  private void setResourceTypes(@NotNull MapValidationConfiguration validationConfiguration) {
    resourceTypes = new ArrayList<>(validationConfiguration.rangeTypesWithResources()
                                                           .stream()
                                                           .flatMap(range -> range.resourceTypes()
                                                                                  .stream()
                                                                                  .map(resource -> new Pair<>(resource,
                                                                                                              range.rangeType())))
                                                           .map(Pair::getKey)
                                                           .toList());
  }
  
  private void setRangeTypes(@NotNull MapValidationConfiguration validationConfiguration) {
    rangeTypes = new ArrayList<>(validationConfiguration.rangeTypesWithResources()
                                                        .stream()
                                                        .map(RangeWithResource::rangeType)
                                                        .toList());
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
