package com.forsrc.common.tool;

import com.forsrc.common.base.IEnumField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ToolEnum {

  private static final Map<String, EnumConverter> mapConverter = new HashMap<>();

  public static void loadEnum(String name, Class<? extends IEnumField> type) {
    Assert.hasText(name, "name is null");
    EnumConverter converter = new EnumConverter(type);
    mapConverter.put(name, converter);
  }

  public static String getName(String enumName, int value) {
    IEnumField t = getEnum(enumName, value);
    return t == null ? null : t.getName();
  }

  public static String getTitle(String enumName, int value) {
    IEnumField t = getEnum(enumName, value);
    return t == null ? null : t.getTitle();
  }

  private static IEnumField getEnum(String enumName, int value) {
    EnumConverter converter = mapConverter.get(enumName);
    return converter == null ? null : converter.convert(value);
  }

  private static class EnumConverter {
    private final Map<Integer, IEnumField> mapEnum = new HashMap<>();

    public EnumConverter(Class<? extends IEnumField> enumType) {
      IEnumField[] enums = enumType.getEnumConstants();
      for (IEnumField e : enums) {
        mapEnum.put(e.getCode(), e);
      }
    }

    public IEnumField convert(int value) {
      return mapEnum.get(value);
    }
  }

}