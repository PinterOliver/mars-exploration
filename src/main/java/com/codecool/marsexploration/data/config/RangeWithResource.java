package com.codecool.marsexploration.data.config;

import com.codecool.marsexploration.data.cell.CellType;

import java.util.Set;

public record RangeWithResource(CellType rangeType, Set<CellType> resourceTypes) {
}
