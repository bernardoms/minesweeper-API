package com.bernardoms.minesweeperapi.model;

import lombok.Data;

@Data
public class CreateGameDTO {
  private int numOfMines;
  private int totalRows;
  private int totalColumns;
  private String player;
}
