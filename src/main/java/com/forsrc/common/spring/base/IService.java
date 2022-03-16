package com.forsrc.common.spring.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by FengJianJun on 2017/5/22.
 */
public interface IService<T> {
  
  int insert(HttpServletRequest request, HttpServletResponse response, T t);

  int update(HttpServletRequest request, HttpServletResponse response, T t);

  int delete(HttpServletRequest request, HttpServletResponse response, T t);

}
