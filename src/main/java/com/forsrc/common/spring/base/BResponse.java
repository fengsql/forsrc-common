package com.forsrc.common.spring.base;

import lombok.Data;

@Data
public class BResponse {

  private int code;

  private String message;
  
  private boolean success;
  
}