package com.codecool.marsexploration.data.config;

import com.codecool.marsexploration.data.cell.CellType;

public record RangeConfiguration(CellType type, int numberOfElements, int numberOfRanges) {
}
