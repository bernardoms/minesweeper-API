package com.bernardoms.minesweeperapi.service;

import com.bernardoms.minesweeperapi.exception.MineSweeperApiException;
import com.bernardoms.minesweeperapi.model.Game;
import com.bernardoms.minesweeperapi.model.GameStatus;
import com.bernardoms.minesweeperapi.model.Tile;
import com.bernardoms.minesweeperapi.repository.GameRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameRepository gameRepository;

  public Game save(Game game) {
    return gameRepository.save(game);
  }

  public Optional<Game> findGameById(ObjectId gameId) {
    return gameRepository.findById(gameId);
  }

  public List<Game> findGamesByPlayer(String playerName) {
    return gameRepository.findGamesByPlayer(playerName);
  }

  public void changeGameStatus(ObjectId gameId, GameStatus status) {
    var game = gameRepository.findById(gameId).orElseThrow(() -> new MineSweeperApiException("Game not found", HttpStatus.NOT_FOUND));
    game.setStatus(status);
    gameRepository.save(game);
  }
}
