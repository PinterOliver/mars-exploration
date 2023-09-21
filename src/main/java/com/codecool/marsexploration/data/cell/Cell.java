package com.codecool.marsexploration.data.cell;

import com.codecool.marsexploration.data.utilities.Coordinate;

public class Cell {
  private final Coordinate position;
  private CellType type;
  
  public Cell(Coordinate position, CellType type) {
    this.position = position;
    this.type = getNotNullType(type);
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
