package com.codecool.marsexploration.data.config;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.List;

public record MapValidationConfiguration(double maxFilledTilesRatio, int minimumMapSize, int maximumMapSize,
                                         double minimumRangeTypeRatio, double minimumResourceTypeRatio,
                                         List<CellType> rangeTypes, List<CellType> resourceTypes) {
}
