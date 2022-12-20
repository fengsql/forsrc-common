package com.forsrc.common.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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

  //  public static <T> T toBean(String bean, Class<T> clazz, String[] timeFields) {
  //    if (Tool.isNull(bean)) {
  //      return null;
  //    }
  //    if (Tool.isNull(timeFields)) {
  //      return JSON.parseObject(bean, clazz);
  //    }
  //    JsonObject jsonObject = toJsonObject(bean);
  //    for (String timeField : timeFields) {
  //      String time = jsonObject.get(timeField).getAsString();
  //      if (StringUtils.isNotBlank(time)) {
  //        jsonObject.addProperty(timeField, Tool.getSecond(time));
  //      }
  //    }
  //    return JSON.parseObject(jsonObject, clazz);
  //  }
  //
  //  public static Map<String, String> paramToMap(String bean) {
  //    Type type = new TypeToken<Map<String, String>>() {}.getType();
  //    return gson.fromJson(bean, type);
  //  }
  //
  //  public static <T> List<T> toList(String jsonData, Class<T> type) {
  //    Gson gson = new Gson();
  //    List<T> result = JSON.fromJson(jsonData, new TypeToken<List<T>>() {}.getType());
  //    return result;
  //  }

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

  //  public static JsonObject toJsonObject(String bean) {
  //    if (Tool.isNull(bean)) {
  //      return null;
  //    }
  //    return jsonParser.parse(bean).getAsJsonObject();
  //  }

  public static String toSimpleText(Object object) {
    String source = toJson(object);
    return Tool.getSimpleText(source);
  }

  //>>>---------------------------------------- toJson ----------------------------------------

  //<<<---------------------------------------- map bean ----------------------------------------

  //  /**
  //   * 将对象装换为map
  //   * @bean bean
  //   * @return
  //   */
  //  public static <T> Map<String, Object> beanToMap(T bean) {
  //    Map<String, Object> map = Maps.newHashMap();
  //    if (bean != null) {
  //      BeanMap beanMap = BeanMap.create(bean);
  //      for (Object key : beanMap.keySet()) {
  //        map.put(key + "", beanMap.get(key));
  //      }
  //    }
  //    return map;
  //  }
  //
  //  /**
  //   * 将map装换为javabean对象。当bean中有字段为数字类型时，此方法有错误。
  //   * @bean map
  //   * @bean bean
  //   * @bean <T>
  //   * @return
  //   */
  //  public static <T> T mapToBean(Map<String, String> map, T bean) {
  //    BeanMap beanMap = BeanMap.create(bean);
  //    beanMap.putAll(map);
  //    return bean;
  //  }

  /**
   * 将一个 Map 对象转化为一个 JavaBean
   * @param type 要转化的类型
   * @param map  包含属性值的 map
   * @return 转化出来的 JavaBean 对象
   */
  public static Object toBean(Map map, Class type) {
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
      Object obj = type.newInstance(); // 创建 JavaBean 对象

      // 给 JavaBean 对象的属性赋值
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      for (int i = 0; i < propertyDescriptors.length; i++) {
        PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
        String propertyName = propertyDescriptor.getName();
        String propertyType = propertyDescriptor.getPropertyType().toString();
        //        System.out.println("propertyName: " + propertyName + "; type: " + propertyDescriptor.getPropertyType().toString());
        if (map.containsKey(propertyName)) {
          // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
          Object value = map.get(propertyName);
          Object[] args = new Object[1];
          setObjectValue(args, value, propertyType);
          try {
            propertyDescriptor.getWriteMethod().invoke(obj, args);
          } catch (Exception e) {
            log.error("toBean invoke error! propertyName: " + propertyName + "; propertyType: " + propertyType + "; value: " + value, e);
          }
        }
      }
      return obj;
    } catch (Exception e) {
      log.error("toBean error!", e);
      return null;
    }
  }

  //  /**
  //   * 将一个 JavaBean 对象转化为一个Map
  //   * @bean bean 要转化的JavaBean 对象
  //   * @return 转化出来的  Map 对象
  //   */
  //  public static Map<String, Object> toMap(Object bean) {
  //    Class type = bean.getClass();
  //    Map returnMap = new HashMap();
  //    try {
  //      BeanInfo beanInfo = Introspector.getBeanInfo(type);
  //      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
  //      for (int i = 0; i < propertyDescriptors.length; i++) {
  //        PropertyDescriptor descriptor = propertyDescriptors[i];
  //        String propertyName = descriptor.getName();
  //        if (!propertyName.equals("class")) {
  //          Method readMethod = descriptor.getReadMethod();
  //          Object result = readMethod.invoke(bean, new Object[0]);
  //          if (result != null) {
  //            returnMap.put(propertyName, result);
  //          } else {
  //            returnMap.put(propertyName, "");
  //          }
  //        }
  //      }
  //    } catch (Exception e) {
  //      log.error(e);
  //      return null;
  //    }
  //    return returnMap;
  //  }

  //>>>---------------------------------------- map bean ----------------------------------------

  //<<<---------------------------------------- toMap ----------------------------------------

  //  public static <T> Map<String, Object> toMap(T object) {
  //    Map<String, Object> map = new HashMap<>();
  //    if (object == null) {
  //      return map;
  //    }
  //    JSONObject jsonObject = JSONObject.fromObject(object);
  //    Iterator iterator = jsonObject.keys();
  //    while (iterator.hasNext()) {
  //      String key = (String) iterator.next();
  //      Object obj = jsonObject.get(key);
  //      //			String value = obj == null ? "" : obj.toString();
  //      map.put(key, obj);
  //    }
  //    return map;
  //  }
  //
  //  public static <T> Map<String, Object> toMap(T object, String[] timeFields) {
  //    Map<String, Object> map = toMap(object);
  //    if (timeFields == null) {
  //      return map;
  //    }
  //    for (String key : timeFields) {
  //      String time = Tool.toString(map.get(key));
  //      if (StringUtils.isNotBlank(time)) {
  //        if (Tool.isLong(time)) {
  //          String value = Tool.getDate(Tool.toLong(time));
  //          map.put(key, value);
  //        } else if (time.indexOf(" ") > 0) {
  //          String value = Tool.toString(Tool.getDate(time));
  //          map.put(key, value);
  //        }
  //      }
  //    }
  //    return map;
  //  }

  //  public static <T> List<Map<String, Object>> toMap(List<T> objects) {
  //    List<Map<String, Object>> maps = new ArrayList<>();
  //    if (objects == null) {
  //      return maps;
  //    }
  //    for (T object : objects) {
  //      Map<String, Object> map = toMap(object);
  //      maps.add(map);
  //    }
  //    return maps;
  //  }
  //
  //  public static <T> List<Map<String, Object>> toMap(List<T> objects, String[] timeFields) {
  //    List<Map<String, Object>> maps = new ArrayList<>();
  //    if (objects == null) {
  //      return maps;
  //    }
  //    for (T object : objects) {
  //      Map<String, Object> map = toMap(object, timeFields);
  //      maps.add(map);
  //    }
  //    return maps;
  //  }

  //  public static Map<String, String> jsonToMap(String json) {
  //    return JSON.parseObject(json, new TypeToken<Map<String, String>>() {}.getType());
  //  }

  public static Map<Object, Object> jsonToMap(String json) {
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
