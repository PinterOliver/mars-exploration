package com.codecool.marsexploration.visuals.logic.actor;

import com.codecool.marsexploration.visuals.logic.cell.Drawable;
import com.codecool.marsexploration.visuals.logic.cell.VirtualCell;

public abstract class Actor implements Drawable {
  private VirtualCell virtualCell;
  
  public Actor(VirtualCell virtualCell) {
    this.virtualCell = virtualCell;
    this.virtualCell.setActor(this);
  }
  
  public void move(int dx, int dy) {
    VirtualCell nextVirtualCell = virtualCell.getNeighbor(dx, dy);
    virtualCell.setActor(null);
    nextVirtualCell.setActor(this);
    virtualCell = nextVirtualCell;
  }
}
