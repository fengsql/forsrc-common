package com.forsrc.common.hadoop.handler;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.forsrc.common.hadoop.configure.HBaseConnectConfigure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
//@Configuration
public class HBaseOperator implements InitializingBean {

  @Resource
  private HBaseConnectConfigure hBaseConnectConfigure;

  private HBaseAdmin admin;

  @Override
  public void afterPropertiesSet() {
    admin = hBaseConnectConfigure.getAdmin();
  }

  /**
   * 创建表，创建表只需要指定列族，不需要指定列
   * @param tableName 表名。
   * @param family    簇。
   * @throws IOException 异常。
   */
  public void createTable(String tableName, String... family) throws IOException {
    HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
    //创建列族
    Arrays.stream(family).forEach(cf -> {
      HColumnDescriptor hcf = new HColumnDescriptor(cf);
      //添加列族
      desc.addFamily(hcf);
    });
    //创建表
    admin.createTable(desc);

    log.info("createTable success. tableName: {}. columnFamily: {}", tableName, family);
  }

  /**
   * 添加数据，对同一个row key进行重新put同一个cell就是修改数据
   * @param params columnFamily/column/value的集合
   */
  /**
   * 添加数据，对同一个row key进行重新put同一个cell就是修改数据。
   * @param tableName 表名。
   * @param rowKey    行键。
   * @param params    columnFamily/column/value的集合。
   * @throws JSONException 异常。
   * @throws IOException   异常。
   */
  public void insert(String tableName, byte[] rowKey, List<JSONObject> params) throws JSONException, IOException {
    HTable table = (HTable) hBaseConnectConfigure.getConnection().getTable(TableName.valueOf(tableName));
    Put put = new Put(rowKey);
    List<Put> puts = new ArrayList<>();
    for (JSONObject param : params) {
      //这里的参数依次为，列族名，列名，值
      long timeStamp = param.getLongValue("timeStamp") > 0 ? param.getLongValue("timeStamp") : HConstants.LATEST_TIMESTAMP;
      byte[] family = param.getString("family").getBytes(); //族名
      byte[] qualifier = param.getString("qualifier").getBytes(); //列名
      byte[] value = param.get("value") != null ? param.get("value").toString().getBytes() : null; //值
      put.addColumn(family, qualifier, timeStamp, value);
      puts.add(put);
    }
    table.put(puts);
  }

  /**
   * 添加数据
   * @param tableName 表名。
   * @param rowKey    行键。
   * @param family    簇。
   * @param colValues 列值。
   * @throws IOException 异常。
   */
  public void insert(String tableName, byte[] rowKey, byte[] family, Map<String, String> colValues) throws IOException {
    HTable table = (HTable) hBaseConnectConfigure.getConnection().getTable(TableName.valueOf(tableName));
    Put put = new Put(rowKey);
    List<Put> puts = new ArrayList<>();
    for (Map.Entry<String, String> entry : colValues.entrySet()) {
      String key = entry.getKey();
      String val = entry.getValue();

      byte[] qualifier = key.getBytes();
      byte[] value = val.getBytes();
      put.addColumn(family, qualifier, value);
      puts.add(put);
    }
    table.put(puts);
  }

  /**
   * 添加数据
   * @param tableName 表名。
   * @param rowKey    行键。
   * @param family    簇。
   * @param qualifier 指定列。
   * @param value     值
   * @throws IOException 异常。
   */
  public void insert(String tableName, byte[] rowKey, byte[] family, byte[] qualifier, byte[] value) throws IOException {
    HTable table = (HTable) hBaseConnectConfigure.getConnection().getTable(TableName.valueOf(tableName));
    Put put = new Put(rowKey);
    put.addColumn(family, qualifier, value);
    table.put(put);
  }

  /**
   * 删除一行数据
   * @param tableName 表名。
   * @param rowKey    行键。
   * @throws IOException 异常。
   */
  public void deleteByRowKey(String tableName, String rowKey) throws IOException {
    HTable table = (HTable) hBaseConnectConfigure.getConnection().getTable(TableName.valueOf(tableName));
    Delete deleteRow = new Delete(rowKey.getBytes()); //删除一个行

    table.delete(deleteRow);
  }

  /**
   * 查询单条数据
   * @param tableName 表名。
   * @param rowKey    行键。
   * @return 返回数据。
   * @throws IOException 异常。
   */
  public Result getData(String tableName, String rowKey) throws IOException {
    HTable table = (HTable) hBaseConnectConfigure.getConnection().getTable(TableName.valueOf(tableName));

    Get get = new Get(Bytes.toBytes(rowKey));
    return table.get(get);
  }

  /**
   * 全表扫描
   * @param tableName 表名。
   * @throws IOException 异常。
   */
  public void scanAll(String tableName) throws IOException {
    HTable table = (HTable) hBaseConnectConfigure.getConnection().getTable(TableName.valueOf(tableName));

    Scan scan = new Scan();
    ResultScanner resultScanner = table.getScanner(scan);
  }

  /**
   * 区间扫描
   * @param tableName 表名。
   * @param startRow  起始行。
   * @param endRow    结束行。
   * @return 返回数据。
   * @throws IOException 异常。
   */
  public Iterator<Result> getData(String tableName, String startRow, String endRow) throws IOException {
    HTable table = (HTable) hBaseConnectConfigure.getConnection().getTable(TableName.valueOf(tableName));

    Scan scan = new Scan();
    scan.withStartRow(startRow.getBytes()); //设置开始行
    scan.withStopRow(endRow.getBytes()); //设置结束行

    ResultScanner resultScanner = table.getScanner(scan);

    return resultScanner.iterator();
  }

  /**
   * 通过传入页码数和每一页的记录条数来获取当页的数据, pageIndex 从 0 开始
   * @param tableName 表名称。
   * @param pageIndex 页码序号，从 0 开始。
   * @param pageSize  每页大小。
   * @return 返回查询的数据。
   * @throws IOException 异常。
   */
  public Iterator<Result> getData(String tableName, int pageIndex, int pageSize) throws IOException {
    String startRowkey = getStartRowKey(tableName, pageIndex, pageSize);
    ResultScanner resultScanner = getDate(tableName, startRowkey, pageSize);
    return resultScanner.iterator();
  }

  private String getStartRowKey(String tableName, int pageIndex, int pageSize) throws IOException {
    if (pageIndex <= 0) {
      return null;
    }
    //解决思路 我们每页获取pageSize+1条记录, 每次遍历记录下最后一条记录的rowkey，就是下一页的开始rowkey
    String startRowKey = null;
    for (int i = 0; i < pageIndex; i++) {
      ResultScanner date = getDate(tableName, startRowKey, pageSize + 1);
      Iterator<Result> iterator = date.iterator();
      Result next = null;
      while (iterator.hasNext()) {
        next = iterator.next();
      }
      startRowKey = next == null ? null : Bytes.toString(next.getRow());
    }
    return startRowKey;
  }

  private ResultScanner getDate(String tableName, String startRowKey, int pageSize) throws IOException {
    Scan scan = new Scan();
    if (!StringUtils.isBlank(startRowKey)) {
      scan.withStartRow(startRowKey.getBytes());
    }
    Filter pageFilter = new PageFilter(pageSize);
    scan.setFilter(pageFilter);
    return getTable(tableName).getScanner(scan);
  }

  private HTable getTable(String tableName) throws IOException {
    return (HTable) hBaseConnectConfigure.getConnection().getTable(TableName.valueOf(tableName));
  }

}
