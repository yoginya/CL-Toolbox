package com.github.samyadaleh.cltoolbox.common.parser;

/**
 * Stores a token along with the line it was found in.
 */
public class Token {
  private final String token;
  private final int lineNumber;

  Token(String token, int lineNumber) {
    this.token = token;
    this.lineNumber = lineNumber;
  }

  public String getString() {
    return token;
  }

  public int getLineNumber() {
    return lineNumber;
  }
}
