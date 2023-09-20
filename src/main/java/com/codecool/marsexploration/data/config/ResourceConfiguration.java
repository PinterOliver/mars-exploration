package com.codecool.marsexploration.data.config;

import com.codecool.marsexploration.data.cell.CellType;

public record ResourceConfiguration(CellType type, int numberOfElements, CellType neighbor) {
}
