package com.bernardoms.minesweeperapi.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "game")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

  @Id
  private ObjectId gameId;
  private int numOfMines;
  private int clicks;
  private int totalRows;
  private int totalColumns;
  private GameStatus status;
  private String player;
  private List<Tile> tiles;

  @CreatedDate
  private LocalDateTime created;
  @LastModifiedDate
  private LocalDateTime updated;
}
