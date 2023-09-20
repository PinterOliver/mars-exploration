package com.codecool.marsexploration.visuals;

import com.codecool.marsexploration.visuals.logic.GameMap;
import com.codecool.marsexploration.visuals.logic.MapLoader;
import com.codecool.marsexploration.visuals.logic.cell.VirtualCell;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class Main extends Application {
  GameMap map;
  Canvas canvas;
  GraphicsContext context;
  
  public Main() throws FileNotFoundException {
    map = MapLoader.loadMap(com.codecool.marsexploration.Application.getFilePath());
    canvas = new Canvas(map.getWidth() * Tiles.BLOCK_HEIGHT, map.getHeight() * Tiles.BLOCK_HEIGHT);
    context = canvas.getGraphicsContext2D();
  }
  
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(canvas);
    Scene scene = new Scene(borderPane);
    
    primaryStage.setScene(scene);
    refresh();
    scene.setOnKeyPressed(this::onKeyPressed);
    
    primaryStage.setTitle("Mars Exploration");
    primaryStage.show();
  }
  
  private void onKeyPressed(KeyEvent keyEvent) {
    if (map.getPlayer() != null) {
      switch (keyEvent.getCode()) {
        case UP -> {
          map.movePlayer(0, -1);
          refresh();
        }
        case DOWN -> {
          map.movePlayer(0, 1);
          refresh();
        }
        case LEFT -> {
          map.movePlayer(-1, 0);
          refresh();
        }
        case RIGHT -> {
          map.movePlayer(1, 0);
          refresh();
        }
      }
    }
  }
  
  private void refresh() {
    context.setFill(Color.BLACK);
    context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    for (int x = 0; x < map.getWidth(); x++) {
      for (int y = 0; y < map.getHeight(); y++) {
        VirtualCell virtualCell = map.getCell(x, y);
        if (virtualCell.getActor() != null) {
          Tiles.drawTile(context, virtualCell.getActor(), x, y);
        } else {
          Tiles.drawTile(context, virtualCell, x, y);
        }
      }
    }
  }
}
