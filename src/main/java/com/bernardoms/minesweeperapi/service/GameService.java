package com.bernardoms.minesweeperapi.service;

import com.bernardoms.minesweeperapi.dto.GameDTO;
import com.bernardoms.minesweeperapi.exception.MineSweeperApiException;
import com.bernardoms.minesweeperapi.model.Game;
import com.bernardoms.minesweeperapi.model.GameStatus;
import com.bernardoms.minesweeperapi.repository.GameRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameRepository gameRepository;
  private final ModelMapper modelMapper;

  public GameDTO save(GameDTO gameDTO) {
    var game = modelMapper.map(gameDTO, Game.class);
    return modelMapper.map(gameRepository.save(game), GameDTO.class);
  }

  public Optional<GameDTO> findGameById(ObjectId gameId) {
    var maybeGame = gameRepository.findById(gameId);
    return maybeGame.map(game -> modelMapper.map(game, GameDTO.class));
  }

  public List<GameDTO> findGamesByPlayer(String playerName) {
    return gameRepository.findGamesByPlayer(playerName).stream()
        .map(g -> modelMapper.map(g, GameDTO.class))
        .collect(Collectors.toList());
  }

  public void changeGameStatus(ObjectId gameId, GameStatus status) {
    var game = gameRepository.findById(gameId).orElseThrow(() -> new MineSweeperApiException("Game not found", HttpStatus.NOT_FOUND));
    game.setStatus(status);
    gameRepository.save(game);
  }
}
