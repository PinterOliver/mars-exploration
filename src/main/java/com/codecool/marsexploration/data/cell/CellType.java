package com.codecool.marsexploration.data.cell;

public enum CellType {
  VOID("x"),
  EMPTY(" "),
  MOUNTAIN("^"),
  PIT("#"),
  MINERAL("*"),
  WATER("~");
  private final String symbol;
  
  CellType(String symbol) {
    this.symbol = symbol;
  }
  
  public String getSymbol() {
    return symbol;
  }
}
