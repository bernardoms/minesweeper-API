package com.bernardoms.minesweeperapi.dto;

import lombok.Data;

@Data
public class CreateGameDTO {
  private int numOfMines;
  private int totalRows;
  private int totalColumns;
  private String player;
}
