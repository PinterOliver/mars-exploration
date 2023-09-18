package com.codecool.marsexploration.service.utilities;

import java.util.*;

public class PickImpl implements Pick {
  private final Random random;
  
  public PickImpl(Random random) {
    this.random = random;
  }
  
  @Override
  public <T> Optional<T> from(Collection<T> options) {
    if (options == null || options.isEmpty()) {
      return Optional.empty();
    }
    List<T> optionList = new ArrayList<>(options);
    int index = random.nextInt(0, optionList.size());
    T result = optionList.get(index);
    return Optional.of(result);
  }
  
  @Override
  public <T> Optional<T> from(T[] options) {
    if (options == null) {
      return Optional.empty();
    }
    return from(Arrays.stream(options).toList());
  }
}
