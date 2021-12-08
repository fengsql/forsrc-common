package com.forsrc.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class HttpException extends RuntimeException {
  private HttpStatus httpStatus;
  private String message;

  public HttpException(HttpStatus httpStatus) {
    this.setHttpStatus(httpStatus);
  }

  public HttpException(HttpStatus httpStatus, String message) {
    this.setHttpStatus(httpStatus);
    this.setMessage(message);
  }

  public HttpException(HttpStatus httpStatus, String message, Throwable cause) {
    super(cause);
    this.setHttpStatus(httpStatus);
    this.setMessage(message);
  }

  public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.setMessage(message);
  }

  public HttpException(String message, Throwable cause) {
    super(message, cause);
    this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    this.setMessage(message);
  }

  public HttpException(String message) {
    this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    this.setMessage(message);
  }

  public HttpException(Throwable cause) {
    super(cause);
    this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    this.setMessage(message);
  }
}