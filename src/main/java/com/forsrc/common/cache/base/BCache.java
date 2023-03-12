package com.forsrc.common.cache.base;

import com.forsrc.common.constant.ConfigCommon;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolRedis;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class BCache {

  private static final String project_seperator = ":";
  private static final String keyPrefix = ":";

  private String keyName;

  public void initialize(String keyName) {
    this.keyName = keyName;
  }

  // <<----------------------- protected -----------------------

  // <<<----------------------- getKey -----------------------
  //value
  protected String getKey(String name, String value) {
    return ConfigCommon.redis.cachePrefix + project_seperator + //
      name + keyPrefix + Tool.toString(value);
  }

  //index
  protected String getKey(String name, String field, String value) {
    return ConfigCommon.redis.cachePrefix + project_seperator + //
      name + keyPrefix + //
      field + keyPrefix + Tool.toString(value);
  }

  protected String getKey(String name, Map<String, String> map) {
    if (map == null || map.size() == 0) {
      return name;
    }
    StringBuilder stringBuilder = new StringBuilder();
    int index = 0;
    int size = map.size();
    stringBuilder.append(ConfigCommon.redis.cachePrefix) //
      .append(project_seperator) //
      .append(name) //
      .append(keyPrefix);
    for (Map.Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      String val = entry.getValue();
      stringBuilder.append(key) //
        .append(keyPrefix) //
        .append(val);
      if (index < size - 1) {
        stringBuilder.append(keyPrefix);
      }
      index++;
    }
    return stringBuilder.toString();
  }

  // >>>----------------------- getKey -----------------------

  // <<<----------------------- index -----------------------
  //one
  protected void setIndexCache(String primaryValue, String indexName, String indexValue) {
    if (Tool.isNull(primaryValue)) {
      return;
    }
    String key = getKeyIndex(indexName, indexValue);
    setRedis(key, primaryValue);
  }

  protected String getIndexCache(String indexName, String indexValue) {
    String key = getKeyIndex(indexName, indexValue);
    return getRedis(key);
  }

  protected void deleteIndex(String indexName, String indexValue) {
    String key = getKeyIndex(indexName, indexValue);
    delRedis(key);
  }

  //more
  protected void setIndexCache(String primaryValue, Map<String, String> map) {
    if (Tool.isNull(primaryValue)) {
      return;
    }
    String key = getKeyIndex(map);
    setRedis(key, primaryValue);
  }

  protected String getIndexCache(Map<String, String> map) {
    String key = getKeyIndex(map);
    return getRedis(key);
  }

  protected void deleteIndex(Map<String, String> map) {
    String key = getKeyIndex(map);
    delRedis(key);
  }

  protected String getKeyIndex(String indexName, String indexValue) {
    return getKey(keyName, indexName, indexValue);
  }

  protected String getKeyIndex(Map<String, String> map) {
    return getKey(keyName, map);
  }

  // >>>----------------------- index -----------------------

  // <<<----------------------- redis -----------------------

  protected void setRedis(String key, String value) {
    if (Tool.isNull(key)) {
      log.warn("key is null!");
      return;
    }
    if (!ToolRedis.set(key, value, ConfigCommon.redis.ttl)) {
      log.warn("setRedis fail. key: {}", key);
    }
    log.debug("setRedis ok. key: {}.", key);
  }

  protected void delRedis(String key) {
    if (Tool.isNull(key)) {
      log.warn("key is null!");
      return;
    }
    if (!ToolRedis.del(key)) {
      log.warn("delRedis fail. key: {}", key);
    }
    log.debug("delRedis ok. key: {}.", key);
  }

  protected String getRedis(String key) {
    if (Tool.isNull(key)) {
      log.warn("key is null!");
      return null;
    }
    Object object = ToolRedis.get(key);
    if (object == null) {
      return null;
    }
    log.debug("getRedis ok. key: {}.", key);
    return (String) object;
  }

  protected void refreshExpire(String key) {
    if (ConfigCommon.redis.refresh <= 0) {
      return;
    }
    long time = ToolRedis.getExpire(key);
    if (time <= 0 || time > ConfigCommon.redis.refresh) {
      return;
    }
    synchronized (this) {
      ToolRedis.setExpire(key, ConfigCommon.redis.ttl);
    }
    log.debug("refreshExpire ok. key: {}.", key);
  }

  // >>>----------------------- redis -----------------------

  // >>----------------------- protected -----------------------

  // <<----------------------- private -----------------------

  // >>----------------------- private -----------------------

}