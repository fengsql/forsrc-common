package com.forsrc.common.exception;

import com.forsrc.common.constant.Code;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class CommonException extends RuntimeException {
  private static final long serialVersionUID = -8913379494715549864L;
  private Integer code;
  private String message;

  public CommonException() {

  }

  public CommonException(Code code) {
    this.setCode(code.getCode());
    this.setMessage(code.getMsg());
  }

  public CommonException(int code, String message) {
    super(message);
    this.message = message;
    this.code = code;
  }

  public CommonException(int code, String message, Throwable cause) {
    super(cause);
    this.message = message;
    this.code = code;
//    log.error(message);
  }

  public CommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.message = message;
  }

  public CommonException(String message, Throwable cause) {
    super(message, cause);
    this.message = message;
  }

  public CommonException(String message) {
    super(message);
    this.message = message;
  }

  public CommonException(Throwable cause) {
    super(cause);
    this.message = cause.getMessage();
  }
}