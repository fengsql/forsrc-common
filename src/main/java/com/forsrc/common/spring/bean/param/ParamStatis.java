package com.forsrc.common.spring.bean.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ParamStatis {

  private Date[] dateTime;

}