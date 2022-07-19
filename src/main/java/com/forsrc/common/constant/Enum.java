package com.forsrc.common.constant;

import com.forsrc.common.tool.Tool;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Enum {

  public enum ExportFieldType {
    constant_(1, "constant"),
    integer_(2, "integer"),
    long_(3, "long"),
    decimal_(4, "decimal"),
    datetime_(5, "datetime"),
    string_(6, "string"),
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
      return Tool.getValue(mapName, name);
    }
  }

}