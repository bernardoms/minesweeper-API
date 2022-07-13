package com.bernardoms.minesweeperapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGameDTO {
  //If not passed, will be used a default
  private int numOfMines = 5;
  private int totalRows = 5;
  private int totalColumns = 5;
  @NotBlank
  private String player;
}
