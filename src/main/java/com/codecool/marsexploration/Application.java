package com.codecool.marsexploration;

import com.codecool.marsexploration.service.filewriter.EasyFileWriter;
import com.codecool.marsexploration.service.filewriter.EasyFileWriterImpl;
import com.codecool.marsexploration.service.input.Input;
import com.codecool.marsexploration.service.input.InputImpl;
import com.codecool.marsexploration.service.logger.ConsoleLogger;
import com.codecool.marsexploration.service.logger.Logger;
import com.codecool.marsexploration.service.utilities.Pick;
import com.codecool.marsexploration.service.utilities.PickImpl;

import java.util.Random;
import java.util.Scanner;

public class Application {
  private static final Logger LOGGER = new ConsoleLogger();
  private static final Scanner SCANNER = new Scanner(System.in);
  private static final Input INPUT = new InputImpl(SCANNER, LOGGER);
  private static final EasyFileWriter FILE_WRITER = new EasyFileWriterImpl(LOGGER);
  private static final Random RANDOM = new Random();
  private static final Pick PICK = new PickImpl(RANDOM);
  
  public static void main(String[] args) {
  }
}
