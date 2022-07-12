package com.bernardoms.minesweeperapi.integration;

import com.bernardoms.minesweeperapi.model.Game;
import com.bernardoms.minesweeperapi.model.GameStatus;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public abstract class IntegrationTest {

  private static boolean alreadySaved = false;

  @Autowired
  MongoTemplate mongoTemplate;

  @BeforeEach
  public void setUp() {

    if (alreadySaved) {
      return;
    }
    mongoTemplate
        .save(createGame("507f191e810c19729de860eb", "test-player"));

    mongoTemplate
        .save(createGame("507f191e810c19729de860ec", "test-player3"));

    alreadySaved = true;
  }

  private Game createGame(String id, String playerName) {
    var game = new Game();
    game.setStatus(GameStatus.IN_PROGRESS);
    game.setGameId(new ObjectId(id));
    game.setTotalRows(10);
    game.setTotalColumns(10);
    game.setPlayer(playerName);
    game.setNumOfMines(5);
    game.start();
    return game;
  }
}
