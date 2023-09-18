package com.codecool.marsexploration.data.cell;

import com.codecool.marsexploration.data.map.Area;
import com.codecool.marsexploration.data.utilities.Coordinate;

public class Cell {
  private final Coordinate position;
  private final Area area;
  private CellType type;
  
  public Cell(Coordinate position, CellType type, Area area) {
    this.position = position;
    this.type = getNotNullType(type);
    this.area = area;
  }
  
  @Override
  public String toString() {
    return type.getSymbol();
  }
  
  public Coordinate getPosition() {
    return position;
  }
  
  public CellType getType() {
    return type;
  }
  
  public void setType(CellType type) {
    this.type = getNotNullType(type);
  }
  
  private CellType getNotNullType(CellType type) {
    if (type == null) {
      return CellType.EMPTY;
    }
    return type;
  }
}
