package com.codecool.marsexploration.data.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.utilities.Coordinate;

public class Area {
  protected final int height;
  protected final int width;
  protected final Cell[][] cells;
  
  public Area(int height, int width) {
    this.height = height;
    this.width = width;
    this.cells = new Cell[this.height][this.width];
    fillCellsWithEmptyCells();
  }
  
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        stringBuilder.append(cells[row][column].toString());
      }
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }

  public Cell getCell(Coordinate coordinate){
    return cells[coordinate.row()][coordinate.column()];
  }

  public void setCell(Coordinate coordinate, CellType type){
    cells[coordinate.row()][coordinate.column()].setType(type);
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  private void fillCellsWithEmptyCells() {
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        cells[row][column] = new Cell(new Coordinate(row, column), CellType.EMPTY, this);
      }
    }
  }
}
