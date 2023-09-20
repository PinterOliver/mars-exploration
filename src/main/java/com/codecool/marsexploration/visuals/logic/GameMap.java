package com.codecool.marsexploration.visuals.logic;

import com.codecool.marsexploration.visuals.logic.actor.Player;
import com.codecool.marsexploration.visuals.logic.cell.VirtualCell;
import com.codecool.marsexploration.visuals.logic.cell.VirtualCellType;

public class GameMap {
  private final int width;
  private final int height;
  private final VirtualCell[][] virtualCells;
  private Player player;
  
  public GameMap(int width, int height, VirtualCellType defaultVirtualCellType) {
    this.width = width;
    this.height = height;
    virtualCells = new VirtualCell[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        virtualCells[x][y] = new VirtualCell(this, x, y, defaultVirtualCellType);
      }
    }
  }
  
  public VirtualCell getCell(int x, int y) {
    return virtualCells[x][y];
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public void setPlayer(Player player) {
    this.player = player;
  }
  
  public int getWidth() {
    return width;
  }
  
  public int getHeight() {
    return height;
  }
  
  public void movePlayer(int dx, int dy) {
    player.move(dx, dy);
  }
}
