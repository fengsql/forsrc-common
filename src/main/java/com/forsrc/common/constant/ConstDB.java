package com.forsrc.common.constant;

public class ConstDB {

  // 连接数据库类型，0 null，1 Mysql，2 SQLServer, 3 Oracle，4 Timesten，5 h2，6 sqlite
  public static final int dbType_null = 0;
  public static final int dbType_mysql = 1;
  public static final int dbType_sqlserver = 2;
  public static final int dbType_oracle = 3;
  public static final int dbType_timesten = 4;
  public static final int dbType_h2 = 5;
  public static final int dbType_sqlite = 6;
  public static final int dbType_kylin = 7;

  public static final class tableType {
    public static final int table = 0;
    public static final int view = 1;
  }

  public static final class layerType {
    public static final int dao = 1;
    public static final int service = 2;
  }

  public static final class action {
    public static final int insert = 1;
    public static final int update = 2;
    public static final int updateCondition = 3;
    public static final int delete = 4;
    public static final int select = 5;
    public static final int queryCount = 6;
    public static final int query = 7;
    public static final int queryMap = 8;
    public static final int queryTable = 9;
    public static final int queryView = 10;
    public static final int upload = 11;
  }

  public static final class sqlType {
    public static final int insert = 1;
    public static final int update = 2;
    public static final int delete = 3;
  }

  public static final class format {
    public static final int nil = 0;
    public static final int datetime = 1;
    public static final int date = 2;
    public static final int time = 3;
    public static final int money = 4;
  }

}
