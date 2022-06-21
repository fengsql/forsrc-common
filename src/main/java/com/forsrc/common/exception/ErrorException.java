package com.forsrc.common.exception;

import com.forsrc.common.constant.Code;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * ErrorException 异常，HttpStatus 值为 500，body 部分的 code 为错误码。
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorException extends RuntimeException {
  private Integer code;
  private String message;

  public ErrorException(Code code) {
    this.setCode(code.getCode());
    this.setMessage(code.getMsg());
  }

  public ErrorException(Code code, String message) {
    this.setCode(code.getCode());
    this.setMessage(message);
  }

  public ErrorException(int code, String message) {
    this.setCode(code);
    this.setMessage(message);
  }

  public ErrorException(int code, String message, Throwable cause) {
    super(cause);
    this.setCode(code);
    this.setMessage(message);
  }

  public ErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.setCode(Code.ERROR_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public ErrorException(String message, Throwable cause) {
    super(message, cause);
    this.setCode(Code.ERROR_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public ErrorException(String message) {
    //    super(message);
    this.setCode(Code.ERROR_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public ErrorException(Throwable cause) {
    super(cause);
    this.setCode(Code.ERROR_EXCEPTION.getCode());
    this.setMessage(message);
  }
}