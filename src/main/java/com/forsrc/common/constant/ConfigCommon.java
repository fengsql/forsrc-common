package com.forsrc.common.constant;

import com.forsrc.common.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigCommon {

  public static class redis {

    public static String cachePrefix;
    public static long ttl;
    public static long refresh;
    public static boolean cacheInsert;

  }

  //redis
  @Value("${redis.cache-prefix:}")
  public void setRedis_cachePrefix(String value) {
    redis.cachePrefix = Tool.toString(value);
  }

  @Value("${redis.ttl:3600}")
  public void setRedis_ttl(String value) {
    redis.ttl = Tool.toLong(value);
  }

  @Value("${redis.refresh:3500}")
  public void setRedis_refresh(String value) {
    redis.refresh = Tool.toLong(value);
  }

  @Value("${redis.cache-insert:true}")
  public void setRedis_cacheInsert(String value) {
    redis.cacheInsert = Tool.toBoolean(value);
  }

}

