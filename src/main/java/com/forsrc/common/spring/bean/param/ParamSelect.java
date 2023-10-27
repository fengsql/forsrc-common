package com.forsrc.common.spring.bean.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ParamSelect {

  private Integer primaryId;

  private Integer tableType;

  private String fieldName;

  private String condition;

}