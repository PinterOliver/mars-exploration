package com.codecool.marsexploration.service.input;

import java.util.function.Function;

public interface Input {
  <T> T get(Function<String, T> stringParsingFunction);
}
