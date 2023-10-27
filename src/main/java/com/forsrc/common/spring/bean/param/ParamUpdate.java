package com.forsrc.common.spring.bean.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ParamUpdate {

  private Integer primaryId;
  
	private String updatePair;

  private String condition;

}