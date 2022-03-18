package com.forsrc.common.spring.type;

import com.forsrc.common.tool.Tool;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(value = String.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public abstract class BaseEnumTypeHandler extends BaseTypeHandler<String> {

  /**
   * 将 java 类型转换为数据库类型
   */
  @Override
  public void setNonNullParameter(PreparedStatement preparedStatement, int paramIndex, String type, JdbcType jdbcType) throws SQLException {
    preparedStatement.setInt(paramIndex, Tool.toInt(type));
  }

  /**
   * 通过字段名获取数据库值后，将数据库类型转换为 java 类型
   */
//  @Override
//  public String getNullableResult(ResultSet resultSet, String fieldName) throws SQLException {
//    return null;
//  }

  /**
   * 通过字段索引获取数据库值后，将数据库类型转换为 java 类型
   */
  @Override
  public String getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
    return null;
  }

  /**
   * 通过存储过程获取数据库值后，将数据库类型转换为 java 类型
   */
  @Override
  public String getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
    return null;
  }
}