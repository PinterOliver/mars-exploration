package com.codecool.marsexploration.service.filewriter;

import com.codecool.marsexploration.service.logger.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EasyFileWriterImpl implements EasyFileWriter {
  private final Logger logger;
  
  public EasyFileWriterImpl(Logger logger) {
    this.logger = logger;
  }
  
  @Override
  public void writeFile(String filePath, String content) {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(filePath));
      writer.println(content);
      writer.close();
    } catch (IOException e) {
      logger.logError(e.getMessage());
    }
  }
}
