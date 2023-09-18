package com.codecool.marsexploration.data.utilities;

import org.jetbrains.annotations.NotNull;

public record Coordinate(int row, int column) {
  public static @NotNull Coordinate add(Coordinate coordinate1, Coordinate coordinate2) {
    coordinate1 = getNotNullCoordinate(coordinate1);
    coordinate2 = getNotNullCoordinate(coordinate2);
    return new Coordinate(coordinate1.row + coordinate2.row, coordinate1.column + coordinate2.column);
  }
  
  private static @NotNull Coordinate getNotNullCoordinate(Coordinate coordinate) {
    if (coordinate == null) {
      return new Coordinate(0, 0);
    }
    return coordinate;
  }
  
  @Override
  public String toString() {
    return "[row: %d, column: %d]".formatted(row, column);
  }
  
  public @NotNull Coordinate add(Coordinate coordinate) {
    return Coordinate.add(this, coordinate);
  }
}
