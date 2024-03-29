package com.forsrc.common.exception;

import com.forsrc.common.constant.Code;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * CommonException 异常，HttpStatus 值为 200，body 部分的 code 为错误码。
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class CommonException extends RuntimeException {
  private Integer code;
  private String message;

  public CommonException(Code code) {
    super(code.getMsg());
    this.setCode(code.getCode());
    this.setMessage(code.getMsg());
  }

  public CommonException(String message) {
    super(message);
    this.setCode(Code.COMMON_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public CommonException(Code code, String message) {
    super(message);
    this.setCode(code.getCode());
    this.setMessage(message);
  }

  public CommonException(int code, String message) {
    super(message);
    this.setCode(code);
    this.setMessage(message);
  }

  public CommonException(int code, String message, Throwable cause) {
    super(cause);
    this.setCode(code);
    this.setMessage(message);
  }

  public CommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.setCode(Code.COMMON_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public CommonException(String message, Throwable cause) {
    super(message, cause);
    this.setCode(Code.COMMON_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public CommonException(Throwable cause) {
    super(cause);
    this.setCode(Code.COMMON_EXCEPTION.getCode());
    this.setMessage(message);
  }
}