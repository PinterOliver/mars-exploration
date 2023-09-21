package com.codecool.marsexploration.visuals.logic.cell;

public enum VirtualCellType {
  EMPTY("empty"),
  FLOOR("floor"),
  MINERAL("mineral"),
  PIT("pit"),
  WATER("water"),
  MOUNTAIN("mountain"),
  GOLD("gold");
  private final String tileName;
  
  VirtualCellType(String tileName) {
    this.tileName = tileName;
  }
  
  public String getTileName() {
    return tileName;
  }
}
