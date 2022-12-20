package com.forsrc.common.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Enum {

  public enum TopicType {  //生产方式
    text_(1, "text"),  //
    pack_(2, "pack"),  //
    ;

    @Getter
    private final int id;
    @Getter
    private final String name;
    @Getter
    private static final Map<String, TopicType> mapName = new HashMap<>();
    @Getter
    private static final Map<Integer, TopicType> mapValue = new HashMap<>();

    static {
      for (TopicType item : TopicType.values()) {
        mapName.put(item.getName(), item);
        mapValue.put(item.getId(), item);
      }
    }

    TopicType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static TopicType get(int id) {
      return mapValue.get(id);
    }

    public static TopicType get(String name) {
      return mapName.get(name);
    }
  }

  public enum StoreMode {  //入库方式
    sql_(1, "sql"),  //
    copy_(2, "copy"),  //
    ;

    @Getter
    private final int id;
    @Getter
    private final String name;
    @Getter
    private static final Map<String, StoreMode> mapName = new HashMap<>();
    @Getter
    private static final Map<Integer, StoreMode> mapValue = new HashMap<>();

    static {
      for (StoreMode item : StoreMode.values()) {
        mapName.put(item.getName(), item);
        mapValue.put(item.getId(), item);
      }
    }

    StoreMode(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static StoreMode get(int id) {
      return mapValue.get(id);
    }

    public static StoreMode get(String name) {
      return mapName.get(name);
    }
  }

  public enum ExportFieldType {
    constant_(1, "constant"),  //
    integer_(2, "integer"),  //
    long_(3, "long"),  //
    decimal_(4, "decimal"),  //
    datetime_(5, "datetime"),  //
    string_(6, "string"),  //
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static final Map<String, ExportFieldType> mapName = new HashMap<>();
    @Getter
    private static final Map<Integer, ExportFieldType> mapValue = new HashMap<>();

    static {
      for (ExportFieldType item : ExportFieldType.values()) {
        mapName.put(item.getName(), item);
        mapValue.put(item.getId(), item);
      }
    }

    ExportFieldType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static ExportFieldType get(int id) {
      return mapValue.get(id);
    }

    public static ExportFieldType get(String name) {
      return mapName.get(name);
    }
  }

}