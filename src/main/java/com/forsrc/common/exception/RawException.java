package com.forsrc.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * @Description RawException 异常，HttpStatus 值为传入状态码，body 部分的 code 错误码与 HttpStatus 一致。
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class RawException extends RuntimeException {
  private HttpStatus httpStatus;
  private String message;

  public RawException(HttpStatus httpStatus) {
    this.setHttpStatus(httpStatus);
    this.setMessage(null);
  }

  public RawException(String message) {
    this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    this.setMessage(message);
  }

  public RawException(HttpStatus httpStatus, String message) {
    this.setHttpStatus(httpStatus);
    this.setMessage(message);
  }

  public RawException(HttpStatus httpStatus, String message, Throwable cause) {
    super(cause);
    this.setHttpStatus(httpStatus);
    this.setMessage(message);
  }

  public RawException(String message, Throwable cause) {
    super(message, cause);
    this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    this.setMessage(message);
  }

  public RawException(Throwable cause) {
    super(cause);
    this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    this.setMessage(message);
  }

  public RawException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.setMessage(message);
  }
}