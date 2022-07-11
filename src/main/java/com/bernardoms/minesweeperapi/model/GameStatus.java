package com.bernardoms.minesweeperapi.model;

public enum GameStatus {
  IN_PROGRESS, PAUSED, LOSE, WON;

  public boolean endStatus() {
    return this == LOSE || this == WON;
  }
}
