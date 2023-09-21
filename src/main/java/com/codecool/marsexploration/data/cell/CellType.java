package com.codecool.marsexploration.data.cell;

import org.jetbrains.annotations.NotNull;

public enum CellType {
  VOID("x"),
  EMPTY(" "),
  MOUNTAIN("^"),
  PIT("#"),
  MINERAL("*"),
  WATER("~"),
  ALIEN("p"),
  GOLD("g"),
  PLACEHOLDER("h");
  private final String symbol;
  
  CellType(String symbol) {
    this.symbol = symbol;
  }
  
  public String getSymbol() {
    return symbol;
  }
  
  public @NotNull String getName() {
    return toString().toLowerCase();
  }
}
