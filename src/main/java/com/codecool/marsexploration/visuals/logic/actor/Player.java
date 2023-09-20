package com.codecool.marsexploration.visuals.logic.actor;

import com.codecool.marsexploration.visuals.logic.cell.VirtualCell;

public class Player extends Actor {
  public Player(VirtualCell virtualCell) {
    super(virtualCell);
  }
  
  public String getTileName() {
    return "player";
  }
}
