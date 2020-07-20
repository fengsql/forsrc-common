package com.forsrc.common.spring.base;

import java.util.List;
import java.util.Map;

/**
 * Created by FengJianJun on 2017/5/22.
 */
public interface IDao<T> {

  int insert(T t);

  int update(T t);

  int delete(T t);

  T selectOne(T t);

  List<Map<String, Object>> selectList(Map<String, Object> map);

}
