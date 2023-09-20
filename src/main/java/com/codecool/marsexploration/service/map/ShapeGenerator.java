package com.codecool.marsexploration.service.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.map.Area;

public interface ShapeGenerator {
  Area generate(int size, CellType type);
}
