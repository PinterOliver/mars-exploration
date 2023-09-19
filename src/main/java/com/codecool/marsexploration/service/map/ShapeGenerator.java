package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;

public interface ShapeGenerator {
  Cell[][] generate(int size, CellType type);
}
