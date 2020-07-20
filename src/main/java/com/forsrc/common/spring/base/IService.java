package com.forsrc.common.spring.base;

/**
 * Created by FengJianJun on 2017/5/22.
 */
public interface IService<T> {
  
  int insert(T t);

  int update(T t);

  int delete(T t);

}
