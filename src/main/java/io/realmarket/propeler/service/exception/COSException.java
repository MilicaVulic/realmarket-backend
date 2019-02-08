package io.realmarket.propeler.service.exception;

public class COSException extends RuntimeException {
  public COSException(String message) {
    super(message);
  }

  public COSException(String message, Throwable cause) {
    super(message, cause);
  }
}
