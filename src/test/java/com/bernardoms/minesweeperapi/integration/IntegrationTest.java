package com.bernardoms.minesweeperapi.integration;

import com.bernardoms.minesweeperapi.dto.GameDTO;
import com.bernardoms.minesweeperapi.model.Game;
import com.bernardoms.minesweeperapi.model.GameStatus;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public abstract class IntegrationTest {

  private static boolean alreadySaved = false;
  private final ModelMapper modelMapper = new ModelMapper();

  @Autowired
  MongoTemplate mongoTemplate;

  @BeforeEach
  public void setUp() {

    if (alreadySaved) {
      return;
    }
    mongoTemplate
        .save(createGame("507f191e810c19729de860eb", "test-player", GameStatus.IN_PROGRESS));

    mongoTemplate
        .save(createGame("507f191e810c19729de860ec", "test-player3", GameStatus.IN_PROGRESS));

    mongoTemplate
        .save(createGame("507f191e810c19729de861ec", "test-player4", GameStatus.WON));

    alreadySaved = true;
  }

  private Game createGame(String id, String playerName, GameStatus status) {
    var gameDTO = new GameDTO();
    gameDTO.setStatus(status);
    gameDTO.setTotalRows(10);
    gameDTO.setTotalColumns(10);
    gameDTO.setPlayer(playerName);
    gameDTO.setNumOfMines(5);
    gameDTO.start();

    var game = modelMapper.map(gameDTO, Game.class);

    game.setGameId(new ObjectId(id));

    return game;
  }
}
