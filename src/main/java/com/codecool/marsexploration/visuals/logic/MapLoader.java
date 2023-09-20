package com.codecool.marsexploration.visuals.logic;

import com.codecool.marsexploration.visuals.logic.actor.Player;
import com.codecool.marsexploration.visuals.logic.cell.VirtualCell;
import com.codecool.marsexploration.visuals.logic.cell.VirtualCellType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
  public static GameMap loadMap(String filePath) throws FileNotFoundException {
    InputStream is = readFile(filePath);
    Scanner scanner = new Scanner(is);
    int size = scanner.nextInt();
    
    scanner.nextLine(); // empty line
    //        mountains (^), pits (#), minerals (*), and pockets of water (~).
    GameMap map = new GameMap(size, size, VirtualCellType.EMPTY);
    for (int y = 0; y < size; y++) {
      String line = scanner.nextLine();
      for (int x = 0; x < size; x++) {
        if (x < line.length()) {
          VirtualCell virtualCell = map.getCell(x, y);
          switch (line.charAt(x)) {
            case ' ' -> virtualCell.setType(VirtualCellType.EMPTY);
            case '^' -> virtualCell.setType(VirtualCellType.MOUNTAIN);
            case '#' -> virtualCell.setType(VirtualCellType.PIT);
            case '~' -> virtualCell.setType(VirtualCellType.WATER);
            case '*' -> virtualCell.setType(VirtualCellType.MINERAL);
            case 'p' -> {
              virtualCell.setType(VirtualCellType.EMPTY);
              map.setPlayer(new Player(virtualCell));
            }
            default -> throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
          }
        }
      }
    }
    return map;
  }
  
  private static InputStream readFile(String filePath) throws FileNotFoundException {
    File initialFile = new File(filePath);
    InputStream inputStream;
    try {
      inputStream = new FileInputStream(initialFile);
    } catch (FileNotFoundException e) {
      System.out.println("Could not load map file. Try placing it in the App's folder or pass the filename as args.");
      throw new FileNotFoundException("Map file not found.");
    }
    return inputStream;
  }
}
