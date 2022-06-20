package com.forsrc.common.exception;

import com.forsrc.common.constant.Code;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemException extends RuntimeException {
  private Integer code;
  private String message;

  public SystemException(Code code) {
    this.setCode(code.getCode());
    this.setMessage(code.getMsg());
  }

  public SystemException(Code code, String message) {
    this.setCode(code.getCode());
    this.setMessage(message);
  }

  public SystemException(int code, String message) {
    this.setCode(code);
    this.setMessage(message);
  }

  public SystemException(int code, String message, Throwable cause) {
    super(cause);
    this.setCode(code);
    this.setMessage(message);
  }

  public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.setCode(Code.COMMON_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public SystemException(String message, Throwable cause) {
    super(message, cause);
    this.setCode(Code.COMMON_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public SystemException(String message) {
    //    super(message);
    this.setCode(Code.COMMON_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public SystemException(Throwable cause) {
    super(cause);
    this.setCode(Code.COMMON_EXCEPTION.getCode());
    this.setMessage(message);
  }
}