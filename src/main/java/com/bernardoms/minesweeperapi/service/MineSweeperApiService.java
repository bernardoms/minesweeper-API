package com.bernardoms.minesweeperapi.service;

import com.bernardoms.minesweeperapi.dto.CreateGameDTO;
import com.bernardoms.minesweeperapi.dto.GameDTO;
import com.bernardoms.minesweeperapi.dto.GameEventDTO;
import com.bernardoms.minesweeperapi.exception.MineSweeperApiException;
import com.bernardoms.minesweeperapi.model.GameStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MineSweeperApiService {
  private final GameService gameService;

  public GameDTO createGame(CreateGameDTO createGameDTO) {
    GameDTO game = new GameDTO();
    game.setPlayer(createGameDTO.getPlayer());
    game.setNumOfMines(createGameDTO.getNumOfMines());
    game.setTotalColumns(createGameDTO.getTotalColumns());
    game.setTotalRows(createGameDTO.getTotalRows());
    game.start();
    game.setStatus(GameStatus.IN_PROGRESS);

    return gameService.save(game);
  }

  public GameDTO findGameById(ObjectId gameId) {
    return gameService.findGameById(gameId).orElseThrow(() -> new MineSweeperApiException("Game not found", HttpStatus.NOT_FOUND));
  }

  public List<GameDTO> findGamesByPlayer(String playerName) {
    return gameService.findGamesByPlayer(playerName);
  }

  public void resumeGame(ObjectId gameId) {
    checkIfGameEnded(gameId);
    gameService.changeGameStatus(gameId, GameStatus.IN_PROGRESS);
  }

  public void pauseGame(ObjectId gameId) {
    checkIfGameEnded(gameId);
    gameService.changeGameStatus(gameId, GameStatus.PAUSED);
  }

  public GameDTO flagTile(ObjectId gameId, GameEventDTO gameEventDTO) {
    var game = this.findGameById(gameId);

    checkIfGameEnded(gameId);


    if(game.getStatus() == GameStatus.PAUSED) {
      throw new MineSweeperApiException("Game is Paused can't set tiles to visible, need to resume for that",  HttpStatus.BAD_REQUEST);
    }

    game.flagTile(gameEventDTO.getPosX(), gameEventDTO.getPosY());

    return gameService.save(game);
  }

  public GameDTO recognize(ObjectId gameId, GameEventDTO gameEventDTO) {
    var game = this.findGameById(gameId);

    checkIfGameEnded(gameId);

    if(game.getStatus() == GameStatus.PAUSED) {
      throw new MineSweeperApiException("Game is Paused can't set tiles to visible, need to resume for that", HttpStatus.BAD_REQUEST);
    }

    game.recognize(gameEventDTO.getPosX(), gameEventDTO.getPosY());

    return gameService.save(game);
  }

  private void checkIfGameEnded(ObjectId gameId) {
    var game = this.findGameById(gameId);
    //Cant pause a ended game
    if(game.getStatus().endStatus()) {
      throw new MineSweeperApiException("Game is already on end state", HttpStatus.BAD_REQUEST);
    }
  }
}
