package com.bernardoms.minesweeperapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MineSweeperApiException extends RuntimeException {
  private final HttpStatus httpStatus;
  public MineSweeperApiException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
