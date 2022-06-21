package com.forsrc.common.reponse;

import com.forsrc.common.spring.base.BResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResponseBody extends BResponse {

  private Integer code;

  private String message;

  private Boolean success;

  private Object data;

}