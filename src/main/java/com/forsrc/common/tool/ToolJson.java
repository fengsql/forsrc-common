package com.forsrc.common.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by FengJianJun on 2017/5/11.
 */
@Slf4j
public class ToolJson {
  private static final String contentType_default = "json";
  private static final int length_simple_text = 500; //
  //  private static Gson gson = new Gson();
  //  private static JsonParser jsonParser = new JsonParser();

  //<<---------------------------------------- initialize ----------------------------------------

  //>>---------------------------------------- initialize ----------------------------------------

  //<<---------------------------------------- public ----------------------------------------

  //<<<---------------------------------------- toBean ----------------------------------------

  public static <T> T toBean(String json, Class<T> clazz) {
    if (Tool.isNull(json)) {
      return null;
    }
    return JSON.parseObject(json, clazz);
  }

  public static <T> T toBean(String json, Type typeOfT) {
    if (Tool.isNull(json)) {
      return null;
    }
    return JSON.parseObject(json, typeOfT);
  }

  /**
   * 转换 Map 等对象。
   * @param json    json
   * @param typeRef 如：类型
   * @param <T>     泛型
   * @return map 对象
   */
  public static <T> T toBean(String json, TypeReference<T> typeRef) {
    if (Tool.isNull(json)) {
      return null;
    }
    return JSON.parseObject(json, typeRef);
  }

  public static <T> List<T> toList(String json, Class<T> clazz) {
    if (Tool.isNull(json)) {
      return null;
    }
    return JSON.parseArray(json, clazz);
  }

  //>>>---------------------------------------- toBean ----------------------------------------

  //<<<---------------------------------------- toJson ----------------------------------------

  public static String toJson(Map<String, String> map) {
    if (map == null) {
      return null;
    }
    return JSON.toJSONString(map);
  }

  public static String toJson(Object object) {
    if (object == null) {
      return null;
    }
    return JSON.toJSONString(object);
  }

  public static String toJsonPretty(Object object) {
    if (object == null) {
      return null;
    }
    return JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
  }

  public static String toSimpleText(Object object) {
    String source = toJson(object);
    return Tool.getSimpleText(source);
  }

  //>>>---------------------------------------- toJson ----------------------------------------

  //<<<---------------------------------------- toMap ----------------------------------------

  public static Map<Object, Object> toMap(String json) {
    return JSON.parseObject(json, new TypeToken<Map<Object, Object>>() {}.getType());
  }

  //>>>---------------------------------------- toMap ----------------------------------------

  //<<<---------------------------------------- tool ----------------------------------------

  //>>>---------------------------------------- tool ----------------------------------------

  //>>---------------------------------------- public ----------------------------------------

  //<<---------------------------------------- protected ----------------------------------------

  //>>---------------------------------------- protected ----------------------------------------

  //<<---------------------------------------- private ----------------------------------------

  //<<<---------------------------------------- inner ----------------------------------------

  //>>>---------------------------------------- inner ----------------------------------------

  //<<<---------------------------------------- tool ----------------------------------------

  private static void setObjectValue(Object[] args, Object value, String propertyType) {
    if (Tool.equalIgnore(propertyType, "int")) {
      args[0] = Tool.toInt(value);
    } else if (Tool.equalIgnore(propertyType, "double")) {
      args[0] = Tool.toDouble(value);
    } else if (Tool.equalIgnore(propertyType, "float")) {
      args[0] = Tool.toDouble(value);
    } else if (Tool.equalIgnore(propertyType, "class java.util.Date")) {
      args[0] = Tool.toDatetime(value);
    } else {
      args[0] = value;
    }
  }

  //>>>---------------------------------------- tool ----------------------------------------

  //>>---------------------------------------- private ----------------------------------------

  //<<---------------------------------------- get ----------------------------------------

  //>>---------------------------------------- get ----------------------------------------

  //<<---------------------------------------- set ----------------------------------------

  //>>---------------------------------------- set ----------------------------------------

  //<<---------------------------------------- get set ----------------------------------------

  //>>---------------------------------------- get set ----------------------------------------

}
