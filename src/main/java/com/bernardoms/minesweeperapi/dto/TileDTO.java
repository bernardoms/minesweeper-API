package com.bernardoms.minesweeperapi.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TileDTO {
  private boolean isFlagged;
  private boolean isMine;
  private boolean isVisible;
  private int nearMines;
  private int posX;
  private int posY;

  public TileDTO(int posX, int posY) {
    this.posX = posX;
    this.posY = posY;
  }

  public boolean isAdjacent(final TileDTO other) {
    boolean x = Math.abs(this.getPosX() - other.getPosX()) <= 1;
    boolean y = Math.abs(this.getPosY() - other.getPosY()) <= 1;
    return this != other && x && y;
  }

  // I need to see cell without mines. So I call again to continue discover cells.
  public void recognize(final List<TileDTO> tiles) {
    this.setVisible(true);
    var adjacentTiles = tiles.stream()
        .filter(tile -> this.isAdjacent(tile) && tile.isPossibleToRecognized())
        .collect(Collectors.toList());

    if (!adjacentTiles.isEmpty()) {
      //Recursive put all adjacent cells that are possible in a visible status
      adjacentTiles.forEach(tile -> tile.recognize(tiles));
    }
  }

  private boolean isPossibleToRecognized() {
    return !this.isVisible() && !this.isMine() && !this.isFlagged() && this.getNearMines() == 0;
  }
}
