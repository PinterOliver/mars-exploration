package com.codecool.marsexploration.service.input;

import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class FileReaderImpl implements FileReader {
  @Override
  public String getAsString(@NotNull Scanner scanner) {
    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNext()) {
      stringBuilder.append(scanner.nextLine()).append("\n");
    }
    scanner.close();
    return stringBuilder.toString();
  }
}
