package com.forsrc.common.spring.bean.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ParamQuery {

	private int tableType;

	private String queryField;
	
  private int pageIndex;

  private int pageSize;

  private int pageTotal;

  private int startIndex;

  private int rowNum;

  private String condition;

  private String orderBy;

  private String fieldTitle;

  private boolean desc;

  private String sql;

}