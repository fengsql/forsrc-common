package com.forsrc.common.constant;

import com.forsrc.common.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigCommon {

  public static class redis {

    public static boolean cacheInsert;

  }

  //redis
  @Value(value = "redis.cache-insert:true")
  public void setRedis_cacheInsert(String value) {
    redis.cacheInsert = Tool.toBoolean(value);
  }

}

