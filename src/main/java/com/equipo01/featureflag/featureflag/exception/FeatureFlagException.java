package com.equipo01.featureflag.featureflag.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FeatureFlagException extends RuntimeException {

  private final HttpStatus status;
  private final String message;
  private final String description;

  public FeatureFlagException(HttpStatus status, String message, String description) {
    super(message);
    this.status = status;
    this.message = message;
    this.description = description;
  }
}
