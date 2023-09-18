package com.codecool.marsexploration.service.logger;

import java.util.Collection;

public interface Logger {
  void logInfo(String message);
  void logInfo(String[] messages);
  void logInfo(Collection<String> collection);
  void logError(String message);
  void displayMessage(String message);
}
