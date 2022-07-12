package com.bernardoms.minesweeperapi.integration.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bernardoms.minesweeperapi.integration.IntegrationTest;
import com.bernardoms.minesweeperapi.model.Game;
import com.bernardoms.minesweeperapi.model.GameEventDTO;
import com.bernardoms.minesweeperapi.model.GameStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class MineSweeperApiControllerTest extends IntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper mapper = new ObjectMapper();

  private static final String URL_PATH = "/v1/minesweeper/games";

  @Test
  void shouldReturnOkWhenFoundExistingGame() throws Exception {
    var result = mockMvc.perform(get(URL_PATH + "/507f191e810c19729de860eb"))
        .andExpect(status().isOk()).andReturn();

    var responseGame = mapper.readValue(result.getResponse().getContentAsString(), Game.class);

    assertEquals("507f191e810c19729de860eb", responseGame.getGameId().toHexString());
    assertEquals(5, responseGame.getNumOfMines());
    assertEquals(10, responseGame.getTotalColumns());
    assertEquals(10, responseGame.getTotalRows());
    //10x10 board
    assertEquals(100, responseGame.getTiles().size());
    assertEquals("test-player", responseGame.getPlayer());
    assertEquals(0, responseGame.getClicks());
  }

  @Test
  void shouldReturnNotFoundWhenExistingGameNotFound() throws Exception {
    mockMvc.perform(get(URL_PATH + "/507f191e810c19729de860ea"))
        .andExpect(status().isNotFound()).andReturn();
  }

  @Test
  void shouldCreateANewGame() throws Exception {
    var game = new Game();
    game.setStatus(GameStatus.IN_PROGRESS);
    game.setTotalRows(10);
    game.setTotalColumns(10);
    game.setPlayer("test-player2");
    game.setNumOfMines(5);

    mockMvc.perform(post(URL_PATH).content(mapper.writeValueAsString(game))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated()).andExpect(header().exists("location"));
  }

  @Test
  void shouldFlagATile() throws Exception {
    var gameEventDTO = new GameEventDTO();
    gameEventDTO.setPosX(0);
    gameEventDTO.setPosY(0);

    var result = mockMvc.perform(put(URL_PATH + "/507f191e810c19729de860eb/flag").content(mapper.writeValueAsString(gameEventDTO)))
        .andExpect(status().isOk()).andReturn();

    var responseGame = mapper.readValue(result.getResponse().getContentAsString(), Game.class);

    var flaggedTile = responseGame.getTiles().stream().filter(t->t.getPosX() == 0 && t.getPosY() == 0).findFirst();

    assertTrue(flaggedTile.isPresent());
    assertTrue(flaggedTile.get().isFlagged());
  }

  @Test
  void shouldFindAllGamesFromAPlayer() throws Exception {
    var result = mockMvc.perform(get(URL_PATH + "/player/test-player"))
        .andExpect(status().isOk()).andReturn();

    var responseGame = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Game>>() {});

    assertEquals(1, responseGame.size());
  }

  @Test
  void shouldPauseGame() throws Exception {
    mockMvc.perform(put(URL_PATH + "/507f191e810c19729de860ec/pause"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldResumeGame() throws Exception {
    mockMvc.perform(put(URL_PATH + "/507f191e810c19729de860ec/resume"))
        .andExpect(status().isOk());
  }
}
