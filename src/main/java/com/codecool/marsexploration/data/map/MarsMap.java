package com.codecool.marsexploration.data.map;

public class MarsMap extends Area {
  public MarsMap(int size) {
    super(size, size);
  }
  
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(height).append("\n");
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        stringBuilder.append(cells[row][column].toString());
      }
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }
}
