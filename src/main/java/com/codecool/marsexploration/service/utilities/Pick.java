package com.codecool.marsexploration.service.utilities;

import java.util.Collection;
import java.util.Optional;

public interface Pick {
  <T> Optional<T> from(Collection<T> options);
  <T> Optional<T> from(T[] options);
}
