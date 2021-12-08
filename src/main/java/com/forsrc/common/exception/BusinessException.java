package com.forsrc.common.exception;

import com.forsrc.common.constant.Code;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {
  private Integer code;
  private String message;
  private Object data;

  public BusinessException(Code code) {
    this.setCode(code.getCode());
    this.setMessage(code.getMsg());
  }

  public BusinessException(Code code, String message) {
    this.setCode(code.getCode());
    this.setMessage(message);
  }

  public BusinessException(int code, String message) {
    this.setCode(code);
    this.setMessage(message);
  }

  public BusinessException(int code, String message, Object data) {
    this.setCode(code);
    this.setMessage(message);
    this.setData(data);
  }

  public BusinessException(int code, String message, Object data, Throwable cause) {
    super(cause);
    this.setCode(code);
    this.setMessage(message);
    this.setData(data);
  }

  public BusinessException(int code, String message, Throwable cause) {
    super(cause);
    this.setCode(code);
    this.setMessage(message);
  }

  public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.setCode(Code.BUSINESS_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
    this.setCode(Code.BUSINESS_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public BusinessException(String message) {
    this.setCode(Code.BUSINESS_EXCEPTION.getCode());
    this.setMessage(message);
  }

  public BusinessException(Throwable cause) {
    super(cause);
    this.setCode(Code.BUSINESS_EXCEPTION.getCode());
    this.setMessage(message);
  }
}