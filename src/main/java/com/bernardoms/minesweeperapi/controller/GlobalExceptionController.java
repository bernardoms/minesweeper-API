package com.bernardoms.minesweeperapi.controller;

import com.bernardoms.minesweeperapi.exception.MineSweeperApiException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

  //Application Errors
  @ExceptionHandler(MineSweeperApiException.class)
  public ResponseEntity<String> handleException(MineSweeperApiException e) {
    log.warn(e.toString());
    return ResponseEntity.status(e.getHttpStatus()).body(e.getHttpStatus().getReasonPhrase());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handeRuntimeException(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
    Map<String, String> parsedErrors = processAllErrors(fieldErrors, globalErrors);

    log.warn("Request validation failed: {}", parsedErrors);
    return ResponseEntity.unprocessableEntity()
        .body(parsedErrors);
  }

  private Map<String, String> processAllErrors(List<FieldError> fieldErrors, List<ObjectError> globalErrors) {
    // Get all the field errors
    Map<String, String> fieldErrorMessages = fieldErrors.stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "validation error",
            (l, r) -> String.join(", ", l, r)
        ));

    // Get all the object errors
    Map<String, String> globalErrorMessages = globalErrors.stream()
        .collect(Collectors.toMap(
            ObjectError::getCode,
            err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "validation error",
            (l, r) -> String.join(", ", l, r)
        ));

    // Return a merged map of all field and object errors for the response
    return Stream.concat(fieldErrorMessages.entrySet().stream(), globalErrorMessages.entrySet().stream())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (l, r) -> String.join(", ", l, r)
        ));
  }
}
