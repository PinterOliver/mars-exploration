package com.codecool.marsexploration.service.config;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import com.codecool.marsexploration.data.utilities.Interval;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface TilesManager {
  void startManagingTiles(int size, @NotNull MapValidationConfiguration validationConfiguration,
          Collection<CellType> rangeTypes, Collection<CellType> resourceTypes);
  int getRemainingFreeTiles();
  Interval<Integer> getTypeElementInterval(CellType type);
  boolean remove(CellType type, int numberOfElements);
}