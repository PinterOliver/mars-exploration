package com.codecool.marsexploration.visuals.logic.cell;

import com.codecool.marsexploration.visuals.logic.GameMap;
import com.codecool.marsexploration.visuals.logic.actor.Actor;

public class VirtualCell implements Drawable {
  private final GameMap gameMap;
  private final int x;
  private final int y;
  private VirtualCellType type;
  private Actor actor;
  
  public VirtualCell(GameMap gameMap, int x, int y, VirtualCellType type) {
    this.gameMap = gameMap;
    this.x = x;
    this.y = y;
    this.type = type;
  }
  
  @Override
  public String getTileName() {
    return type.getTileName();
  }
  
  public void setType(VirtualCellType type) {
    this.type = type;
  }
  
  public Actor getActor() {
    return actor;
  }
  
  public void setActor(Actor actor) {
    this.actor = actor;
  }
  
  public VirtualCell getNeighbor(int dx, int dy) {
    return gameMap.getCell(x + dx, y + dy);
  }
}
