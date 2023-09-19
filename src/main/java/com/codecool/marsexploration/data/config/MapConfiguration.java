package com.codecool.marsexploration.data.config;

public record MapConfiguration(int size, int numberOfMountains, int numberOfPits, int numberOfMinerals,
                               int numberOfWaters, int numberOfMountainRanges, int numberOfPitCanyons) {
  public int getNumberOfTiles() {
    return numberOfMinerals + numberOfMountains + numberOfWaters + numberOfPits;
  }
}
