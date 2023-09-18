package com.codecool.marsexploration.service.logger;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collection;

public class ConsoleLogger implements Logger {
  @Override
  public void logInfo(String message) {
    logMessage("INFO", message);
  }
  
  @Override
  public void logInfo(String @NotNull [] messages) {
    for (int i = 0; i < messages.length; i++) {
      logInfo(String.format("%d: %s", i + 1, messages[i]));
    }
  }
  
  @Override
  public void logInfo(@NotNull Collection<String> messages) {
    String[] stringArray = messages.toArray(String[]::new);
    logInfo(stringArray);
  }
  
  @Override
  public void logError(String message) {
    logMessage("ERROR", message);
  }
  
  private void displayMessage(String message) {
    System.out.println(message);
  }
  
  private void logMessage(String type, String message) {
    LocalDateTime now = LocalDateTime.now();
    displayMessage(String.format("[%tF %tT] %s: %s", now, now, type, message));
  }
}
