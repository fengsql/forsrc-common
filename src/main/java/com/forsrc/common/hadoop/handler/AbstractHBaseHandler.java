package com.forsrc.common.hadoop.handler;

import com.alibaba.fastjson.JSONObject;
import com.forsrc.common.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public abstract class AbstractHBaseHandler {

  @Resource
  private HBaseOperator hBaseOperator;

  protected void insertHBase(String hBaseTable, String rowKey, List<JSONObject> datas) throws IOException {
    hBaseOperator.insert(hBaseTable, rowKey, datas);
  }

  protected <T> List<T> getData(String tableName, String startRow, String endRow, Class<T> clazz) throws IOException, IllegalAccessException, InstantiationException {
    Iterator<Result> iterator = hBaseOperator.getData(tableName, startRow, endRow);
    return toList(iterator, clazz);
  }

  protected <T> List<T> getData(String tableName, int pageIndex, int pageSize, Class<T> clazz) throws IOException, IllegalAccessException, InstantiationException {
    Iterator<Result> iterator = hBaseOperator.getData(tableName, pageIndex, pageSize);
    return toList(iterator, clazz);
  }

  private <T> List<T> toList(Iterator<Result> iterator, Class<T> clazz) throws IOException, IllegalAccessException, InstantiationException {
    //反射设置对象的值
    List<T> list = new ArrayList<>();
    while (iterator.hasNext()) {
      Result result = iterator.next();
      List<Cell> cells = result.listCells();
      T instance = clazz.newInstance();
      cells.forEach(cell -> {
        //                log.info("cell: {}", cell);
        String colName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
        String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
        //                log.info("col: {}. val: {}", colName, value);
        Field[] fields = instance.getClass().getDeclaredFields();
        int index = 0;
        for (Field field : fields) {
          field.setAccessible(true);
          if (index == 0) {
            setValue(instance, field, Bytes.toString(result.getRow()));
          } else {
            if (colName.equalsIgnoreCase(field.getName())) {
              setValue(instance, field, value);
            }
          }
          index++;
        }
        //        Arrays.stream(fields).forEach(field -> {
        //          field.setAccessible(true);
        //          //                    if (camel(new StringBuffer(colName)).toString().equals(field.getName())) {
        //          if (colName.equalsIgnoreCase(field.getName())) {
        //            setValue(instance, field, value);
        //          }
        //        });
      });
      list.add(instance);
    }

    return list;
  }

  private <T> void setValue(T instance, Field field, String value) {
    try {
      Class<?> type = field.getType();
      if (type == Integer.class) {
        field.set(instance, Tool.toInt(value));
      } else if (type == Long.class) {
        field.set(instance, Tool.toLong(value));
      } else if (type == Double.class) {
        field.set(instance, Tool.toDouble(value));
      } else {
        field.set(instance, Tool.toString(value));
      }
    } catch (IllegalAccessException e) {
      log.error("invoke error", e);
    }
  }

  /**
   * 下划线转驼峰
   * @param str
   * @return
   */
  public static StringBuffer camel(StringBuffer str) {
    Pattern pattern = Pattern.compile("_(\\w)");
    Matcher matcher = pattern.matcher(str);
    StringBuffer sb = new StringBuffer(str);
    if (matcher.find()) {
      sb = new StringBuffer();
      matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
      matcher.appendTail(sb);
    } else {
      return sb;
    }
    return camel(sb);
  }
}
