package com.bernardoms.minesweeperapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tile {
  private boolean isFlagged;
  private boolean isMine;
  private boolean isVisible;
  private int nearMines;
  private int posX;
  private int posY;
}
