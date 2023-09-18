package com.codecool.marsexploration.service.input;

import com.codecool.marsexploration.service.logger.Logger;

import java.util.Scanner;
import java.util.function.Function;

public class InputImpl implements Input {
  private final Scanner scanner;
  private final Logger logger;
  
  public InputImpl(Scanner scanner, Logger logger) {
    this.scanner = scanner;
    this.logger = logger;
  }
  
  @Override
  public <T> T get(Function<String, T> stringParsingFunction) {
    boolean isValid = false;
    T value = null;
    while (!isValid) {
      try {
        String input = getNewInput();
        value = stringParsingFunction.apply(input);
        isValid = true;
      } catch (Exception e) {
        logger.logError(e.getMessage());
      }
    }
    return value;
  }
  
  private String getNewInput() {
    return scanner.nextLine();
  }
}
