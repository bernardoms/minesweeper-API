package com.bernardoms.minesweeperapi.dto;

import com.bernardoms.minesweeperapi.model.GameStatus;
import com.bernardoms.minesweeperapi.model.Tile;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDTO {
  private ObjectId gameId;
  private int numOfMines;
  private int clicks;
  private int totalRows;
  private int totalColumns;
  private GameStatus status;
  private String player;
  private List<TileDTO> tiles;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime created;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime updated;


  public void start() {
    this.tiles = new ArrayList<>();
    for (int i = 0; i < this.getTotalRows(); i++) {
      for (int j = 0; j < this.getTotalColumns(); j++) {
        this.getTiles().add(new TileDTO(i, j));
      }
    }

    //Shuffle for do random mines
    Collections.shuffle(this.getTiles());

    // Get only the num of mines for set the mines tiles
    this.getTiles().stream().limit(this.getNumOfMines()).forEach(cellMine -> cellMine.setMine(true));

    // Sets value for each cell (how many mines has near)
    final Stream<TileDTO> aStreamOfCells = this.getTiles().stream().filter(tile -> !tile.isMine());
    aStreamOfCells.forEach(tile -> tile.setNearMines(this.setNearMines(tile)));
  }

  public void flagTile(int posX, int posY) {
    var tile = this.findTileByXYPos(posX, posY);
    tile.setFlagged(true);
  }

  private int setNearMines(final TileDTO tile) {
    return (int) this.getTiles().stream().filter(other -> tile.isAdjacent(other) && other.isMine()).count();
  }

  private TileDTO findTileByXYPos(final int posX, final int posY) {
    return this.getTiles().stream().filter(cell -> cell.getPosX() == posX && cell.getPosY() == posY).findFirst()
        .orElseThrow(() -> new RuntimeException("Cell not found for the given coordinates"));
  }

  public void recognize(int posX, int posY) {
    var tile = findTileByXYPos(posX, posY);
    this.clicks++;

    if(tile.isMine()) {
      this.setStatus(GameStatus.LOSE);
      return;
    }

    tile.recognize(this.getTiles());

    if(allNotMineCellsAreVisible()) {
      this.setStatus(GameStatus.WON);
    }
  }

  private boolean allNotMineCellsAreVisible() {
    return this.getTiles().stream().filter(cell -> !cell.isMine()).allMatch(TileDTO::isVisible);
  }
}
