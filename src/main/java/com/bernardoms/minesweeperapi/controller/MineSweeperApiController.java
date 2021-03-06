package com.bernardoms.minesweeperapi.controller;

import com.bernardoms.minesweeperapi.dto.CreateGameDTO;
import com.bernardoms.minesweeperapi.dto.GameDTO;
import com.bernardoms.minesweeperapi.dto.GameEventDTO;
import com.bernardoms.minesweeperapi.service.MineSweeperApiService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/minesweeper/games")
public class MineSweeperApiController {

  private final MineSweeperApiService mineSweeperApiService;

  @PostMapping
  public ResponseEntity<ObjectId> createGame(@Valid @RequestBody CreateGameDTO createGameDTO) {
    var createdGame = mineSweeperApiService.createGame(createGameDTO);

    var uriComponents = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{gameId}").buildAndExpand(createdGame.getGameId());

    return ResponseEntity.created(uriComponents.toUri()).build();
  }

  @PutMapping("/{gameId}/flag")
  public GameDTO flag(@PathVariable ObjectId gameId, @RequestBody GameEventDTO gameEventDTO) {
    return mineSweeperApiService.flagTile(gameId, gameEventDTO);
  }

  @GetMapping("/{gameId}")
  public GameDTO findGameById(@PathVariable ObjectId gameId) {
    return mineSweeperApiService.findGameById(gameId);
  }

  @GetMapping("/player/{playerName}")
  public List<GameDTO> findGamesFromPlayer(@PathVariable String playerName) {
    return mineSweeperApiService.findGamesByPlayer(playerName);
  }

  @PutMapping("{gameId}/pause")
  public void pauseGame(@PathVariable ObjectId gameId) {
    this.mineSweeperApiService.pauseGame(gameId);
  }

  @PutMapping("{gameId}/resume")
  public void resumeGame(@PathVariable ObjectId gameId) {
    this.mineSweeperApiService.resumeGame(gameId);
  }

  @PutMapping("{gameId}/recognize")
  public GameDTO recognize(@PathVariable ObjectId gameId, @RequestBody GameEventDTO gameEventDTO) {
    return mineSweeperApiService.recognize(gameId, gameEventDTO);
  }
}
