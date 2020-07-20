package com.forsrc.common.extend.base;

public interface IFieldConvertor {

   default String toSqlDatetime(String fieldName) {
      return "date_format(" + fieldName + ", '%Y-%m-%d %T')";
   }
   
}
