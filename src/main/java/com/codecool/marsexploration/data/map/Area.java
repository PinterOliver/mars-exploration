package com.codecool.marsexploration.data.map;

import com.codecool.marsexploration.data.cell.Cell;
import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.utilities.Coordinate;

import java.util.*;
import java.util.stream.Collectors;

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
  
  public void setCell(Coordinate position, CellType cellType) {
    int row = position.row();
    int column = position.column();
    cells[row][column].setType(cellType);
  }
  
  public Cell getCell(Coordinate position) {
    int row = position.row();
    int column = position.column();
    // System.out.println("width: " + row);
    // System.out.println("column: " + column);
    return cells[row][column];
  }
  
  public int getHeight() {
    return height;
  }
  
  public int getWidth() {
    return width;
  }
  
  public List<Cell> getNeighbours(Coordinate coordinate, int radius) {
    int y = coordinate.row();
    int x = coordinate.column();
    
    List<Cell> neighbours = new ArrayList<>();
    
    for (int i = y - radius; i <= y + radius; i++) {
      for (int j = x - radius; j <= x + radius; j++) {
        boolean validCell = (i != y) && (j != x) && (i < height) && (j < width) && (i > 0) && (j > 0);
        if (validCell) {
          neighbours.add(cells[i][j]);
        }
      }
    }
    return neighbours;
  }
  public Collection<Coordinate> filterCellsByType(CellType type) {
    return getAllCells().stream().filter(cell -> cell.getType() == type).map(Cell::getPosition).toList();
  }
  
  private void fillCellsWithEmptyCells() {
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        cells[row][column] = new Cell(new Coordinate(row, column), CellType.EMPTY, this);
      }
    }
  }
  
  private Set<Cell> getAllCells() {
    return Arrays.stream(cells).flatMap(Arrays::stream).collect(Collectors.toSet());
  }
}
