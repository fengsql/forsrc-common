package com.forsrc.common.db.define;

import com.forsrc.common.spring.base.IDao;
import com.forsrc.common.spring.base.IService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by FengJianJun on 2017/5/22.
 */
@Slf4j
@Data
public class DbEntity<T> {

  private int times = 0;
  private long lastTime = 0;

	private IDao<T> dao;
  private IService<T> service;
	private T vo;
  private int sqlType;
  private int layerType;
  
}
