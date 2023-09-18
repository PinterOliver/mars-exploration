package com.codecool.marsexploration.service.logger;

import java.util.Collection;

public interface Logger {
  void logInfo(String message);
  void logInfo(String[] messages);
  void logInfo(Collection<String> messages);
  void logError(String message);
}
