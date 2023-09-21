package com.codecool.marsexploration.visuals;

import com.codecool.marsexploration.visuals.logic.cell.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
  public static final int BLOCK_HEIGHT = 25;
  public static final int TILE_WIDTH = 32;
  private static final Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
  private static final Map<String, Tile> tileMap = new HashMap<>();
  
  static {
    tileMap.put("empty", new Tile(2, 0));
    tileMap.put("mountain", new Tile(4, 18));
    tileMap.put("floor", new Tile(3, 0));
    tileMap.put("pit", new Tile(13, 17));
    tileMap.put("player", new Tile(14, 20));
    tileMap.put("water", new Tile(14, 18));
    tileMap.put("mineral", new Tile(23, 4));
    tileMap.put("gold", new Tile(16, 22));
    tileMap.put("forest", new Tile(1, 1));
    tileMap.put("grass", new Tile(7, 0));
    tileMap.put("house", new Tile(4, 20));
  }
  
  public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
    Tile tile = tileMap.get(d.getTileName());
    context.drawImage(tileset,
                      tile.x,
                      tile.y,
                      tile.w,
                      tile.h,
                      x * BLOCK_HEIGHT,
                      y * BLOCK_HEIGHT,
                      BLOCK_HEIGHT,
                      BLOCK_HEIGHT);
  }
  
  public static class Tile {
    public final int x, y, w, h;
    
    Tile(int col, int row) {
      x = col * (TILE_WIDTH + 2);
      y = row * (TILE_WIDTH + 2);
      w = TILE_WIDTH;
      h = TILE_WIDTH;
    }
  }
}
