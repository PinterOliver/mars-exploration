package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.Cluster;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.data.utilities.Interval;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class TilesCalculator implements TilesManager {
  private int remainingFreeTiles;
  private int minimumClusterTypeNumber;
  private int minimumResourceNumber;
  private Collection<CellType> clusterTypes;
  private Collection<CellType> resourceTypes;
  
  @Override
  public void startManagingTiles(int size, @NotNull MapValidationConfiguration validationConfiguration) {
    int mapSize = (int) Math.pow(size, 2);
    
    setClusterTypes(validationConfiguration);
    setResourceTypes(validationConfiguration);
    
    remainingFreeTiles = (int) (mapSize * validationConfiguration.maxFilledTilesRatio());
    minimumClusterTypeNumber = (int) (mapSize * validationConfiguration.minimumClusterTypeRatio());
    minimumResourceNumber = (int) (mapSize * validationConfiguration.minimumResourceTypeRatio());
  }
  
  @Override
  public void finishManagingTiles() {
    remainingFreeTiles = 0;
    minimumClusterTypeNumber = 0;
    minimumResourceNumber = 0;
    clusterTypes = null;
    resourceTypes = null;
  }
  
  @Override
  public int getRemainingFreeTiles() {
    return remainingFreeTiles;
  }
  
  @Override
  public Interval<Integer> getTypeElementInterval(CellType type) {
    if (clusterTypes.contains(type)) {
      return getClusterInterval();
    }
    if (resourceTypes.contains(type)) {
      return getResourceInterval();
    }
    return null;
  }
  
  @Override
  public boolean remove(CellType type, int numberOfElements) {
    if (clusterTypes.remove(type) || resourceTypes.remove(type)) {
      remainingFreeTiles -= numberOfElements;
      return true;
    }
    return false;
  }
  
  private void setResourceTypes(@NotNull MapValidationConfiguration validationConfiguration) {
    resourceTypes = new ArrayList<>(validationConfiguration.clusterTypes()
                                                           .stream()
                                                           .flatMap(cluster -> cluster.resourceTypes()
                                                                                      .stream()
                                                                                      .map(resource -> new Pair<>(
                                                                                              resource,
                                                                                              cluster.clusterType())))
                                                           .map(Pair::getKey)
                                                           .toList());
  }
  
  private void setClusterTypes(@NotNull MapValidationConfiguration validationConfiguration) {
    clusterTypes = new ArrayList<>(validationConfiguration.clusterTypes().stream().map(Cluster::clusterType).toList());
  }
  
  private @NotNull Interval<Integer> getClusterInterval() {
    return new Interval<>(minimumClusterTypeNumber, getMaximumClusterTypeNumber());
  }
  
  private @NotNull Interval<Integer> getResourceInterval() {
    return new Interval<>(minimumResourceNumber, getMaximumResourceNumber());
  }
  
  private int getMaximumClusterTypeNumber() {
    return remainingFreeTiles - (clusterTypes.size() - 1) * minimumClusterTypeNumber -
           resourceTypes.size() * minimumResourceNumber;
  }
  
  private int getMaximumResourceNumber() {
    return remainingFreeTiles - (clusterTypes.size()) * minimumClusterTypeNumber -
           (resourceTypes.size() - 1) * minimumResourceNumber;
  }
}
