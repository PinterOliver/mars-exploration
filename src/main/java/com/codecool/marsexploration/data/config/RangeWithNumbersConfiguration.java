package com.codecool.marsexploration.data.config;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Set;

public record RangeWithNumbersConfiguration(CellType type, int numberOfElements, int numberOfRanges,
                                            Set<ResourceConfiguration> resourceTypes) {
}
