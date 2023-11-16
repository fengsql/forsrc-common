package com.forsrc.common.tool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
@ConditionalOnProperty(name = "spring.redis.enable", havingValue = "true", matchIfMissing = true) //
@Slf4j
public final class ToolRedis {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  private static ToolRedis toolRedis;

  @PostConstruct
  public void init() {
    toolRedis = this;
    toolRedis.redisTemplate = this.redisTemplate;
  }

  // <<----------------------- common -----------------------

  /**
   * 指定缓存失效时间
   * @param key  键
   * @param time 时间(秒)
   * @return true 成功，false 失败。
   */
  public static boolean setExpire(String key, long time) {
    try {
      if (time > 0) {
        toolRedis.redisTemplate.expire(key, time, TimeUnit.SECONDS);
      }
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 根据key 获取过期时间
   * @param key 键 不能为null
   * @return 时间(秒) 返回0代表为永久有效
   */
  public static long getExpire(String key) {
    return toLong(toolRedis.redisTemplate.getExpire(key, TimeUnit.SECONDS));
  }

  /**
   * 判断key是否存在
   * @param key 键
   * @return true 存在 false不存在
   */
  public static boolean hasKey(String key) {
    try {
      return toBoolean(toolRedis.redisTemplate.hasKey(key));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 删除缓存
   * @param key 可以传一个值 或多个
   * @return true 成功，false 失败。
   */
  @SuppressWarnings("unchecked")
  public static boolean del(String... key) {
    try {
      if (key != null && key.length > 0) {
        if (key.length == 1) {
          return toBoolean(toolRedis.redisTemplate.delete(key[0]));
        } else {
          return toLong(toolRedis.redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key))) > 0;
        }
      }
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

  // >>----------------------- common -----------------------

  // <<----------------------- string -----------------------

  /**
   * 普通缓存获取
   * @param key 键
   * @return 值
   */
  public static Object get(String key) {
    try {
      return key == null ? null : toolRedis.redisTemplate.opsForValue().get(key);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return null;
    }
  }

  /**
   * 普通缓存放入
   * @param key   键
   * @param value 值
   * @return true成功 false失败
   */
  public static boolean set(String key, Object value) {
    try {
      toolRedis.redisTemplate.opsForValue().set(key, value);
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 普通缓存放入并设置时间
   * @param key   键
   * @param value 值
   * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
   * @return true成功 false 失败
   */
  public static boolean set(String key, Object value, long time) {
    try {
      if (time > 0) {
        toolRedis.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
      } else {
        set(key, value);
      }
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 递增
   * @param key   键
   * @param delta 要增加几(大于0)
   * @return 返回值。
   */
  public static long inc(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递增因子必须大于0");
    }
    return toLong(toolRedis.redisTemplate.opsForValue().increment(key, delta));
  }

  /**
   * 递减
   * @param key   键
   * @param delta 要减少几(小于0)
   * @return 返回值。
   */
  public static long dec(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递减因子必须大于0");
    }
    return toLong(toolRedis.redisTemplate.opsForValue().increment(key, -delta));
  }

  // >>----------------------- string -----------------------

  // <<----------------------- hash -----------------------

  /**
   * HashGet
   * @param key  键 不能为null
   * @param item 项 不能为null
   * @return 值
   */
  public static Object getHash(String key, String item) {
    try {
      return toolRedis.redisTemplate.opsForHash().get(key, item);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return null;
    }
  }

  /**
   * 获取hashKey对应的所有键值
   * @param key 键
   * @return 对应的多个键值
   */
  public static Map<Object, Object> getHash(String key) {
    return toolRedis.redisTemplate.opsForHash().entries(key);
  }

  /**
   * HashSet
   * @param key 键
   * @param map 对应多个键值
   * @return true 成功 false 失败
   */
  public static boolean setHash(String key, Map<String, Object> map) {
    try {
      toolRedis.redisTemplate.opsForHash().putAll(key, map);
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * HashSet 并设置时间
   * @param key  键
   * @param map  对应多个键值
   * @param time 时间(秒)
   * @return true成功 false失败
   */
  public static boolean setHash(String key, Map<String, Object> map, long time) {
    try {
      toolRedis.redisTemplate.opsForHash().putAll(key, map);
      if (time > 0) {
        setExpire(key, time);
      }
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   * @param key   键
   * @param item  项
   * @param value 值
   * @return true 成功 false失败
   */
  public static boolean setHash(String key, String item, Object value) {
    try {
      toolRedis.redisTemplate.opsForHash().put(key, item, value);
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   * @param key   键
   * @param item  项
   * @param value 值
   * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
   * @return true 成功 false失败
   */
  public static boolean setHash(String key, String item, Object value, long time) {
    try {
      toolRedis.redisTemplate.opsForHash().put(key, item, value);
      if (time > 0) {
        setExpire(key, time);
      }
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 判断hash表中是否有该项的值
   * @param key  键 不能为null
   * @param item 项 不能为null
   * @return true 存在 false不存在
   */
  public static boolean hasHash(String key, String item) {
    return toolRedis.redisTemplate.opsForHash().hasKey(key, item);
  }

  /**
   * hash递增 如果不存在,就会创建一个 并把新增后的值返回
   * @param key  键
   * @param item 项
   * @param by   要增加几(大于0)
   * @return 返回值。
   */
  public static double incHash(String key, String item, double by) {
    return toolRedis.redisTemplate.opsForHash().increment(key, item, by);
  }

  /**
   * hash递减
   * @param key  键
   * @param item 项
   * @param by   要减少记(小于0)
   * @return 返回值。
   */
  public static double decHash(String key, String item, double by) {
    return toolRedis.redisTemplate.opsForHash().increment(key, item, -by);
  }

  /**
   * 获取set缓存的长度
   * @param key 键
   * @return 返回长度。
   */
  public static long getHashSize(String key) {
    try {
      return toLong(toolRedis.redisTemplate.opsForHash().size(key));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return 0;
    }
  }

  /**
   * 删除hash表中的值
   * @param key  键 不能为null
   * @param item 项 可以使多个 不能为null
   * @return 0失败，其它为成功。
   */
  public static long delHash(String key, Object... item) {
    try {
      return toLong(toolRedis.redisTemplate.opsForHash().delete(key, item));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return 0;
    }
  }

  // >>----------------------- hash -----------------------

  // <<----------------------- set -----------------------

  /**
   * 根据key获取Set中的所有值
   * @param key 键
   * @return 返回值。
   */
  public static Set<Object> getSet(String key) {
    try {
      return toolRedis.redisTemplate.opsForSet().members(key);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return null;
    }
  }

  /**
   * 根据value从一个set中查询,是否存在
   * @param key   键
   * @param value 值
   * @return true 存在 false不存在
   */
  public static boolean hasSet(String key, Object value) {
    try {
      return toBoolean(toolRedis.redisTemplate.opsForSet().isMember(key, value));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 将数据放入set缓存
   * @param key    键
   * @param values 值 可以是多个
   * @return 成功个数
   */
  public static long setSet(String key, Object... values) {
    try {
      return toLong(toolRedis.redisTemplate.opsForSet().add(key, values));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return 0;
    }
  }

  /**
   * 将set数据放入缓存
   * @param key    键
   * @param time   时间(秒)
   * @param values 值 可以是多个
   * @return 成功个数
   */
  public static long setSet(String key, long time, Object... values) {
    try {
      Long count = toolRedis.redisTemplate.opsForSet().add(key, values);
      if (time > 0) {
        setExpire(key, time);
      }
      return toLong(count);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return 0;
    }
  }

  /**
   * 获取set缓存的长度
   * @param key 键
   * @return 返回长度。
   */
  public static long getSetSize(String key) {
    try {
      return toLong(toolRedis.redisTemplate.opsForSet().size(key));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return 0;
    }
  }

  /**
   * 移除值为value的
   * @param key    键
   * @param values 值 可以是多个
   * @return 移除的个数
   */
  public static long removeSet(String key, Object... values) {
    try {
      return toLong(toolRedis.redisTemplate.opsForSet().remove(key, values));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return 0;
    }
  }

  // >>----------------------- set -----------------------

  // <<----------------------- list -----------------------

  /**
   * 获取list缓存的内容
   * @param key   键
   * @param start 开始
   * @param end   结束 0 到 -1代表所有值
   * @return 返回值。
   */
  public static List<Object> getList(String key, long start, long end) {
    try {
      return toolRedis.redisTemplate.opsForList().range(key, start, end);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return null;
    }
  }

  /**
   * 通过索引 获取list中的值
   * @param key   键
   * @param index 索引 index大于等于0时， 0 表头，1 第二个元素，依次类推；index小于0时，-1，表尾，-2倒数第二个元素，依次类推
   * @return 返回值。
   */
  public static Object getList(String key, long index) {
    try {
      return toolRedis.redisTemplate.opsForList().index(key, index);
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return null;
    }
  }

  /**
   * 将list放入缓存
   * @param key   键
   * @param value 值
   * @return true 成功，false 失败。
   */
  public static boolean setList(String key, Object value) {
    try {
      toolRedis.redisTemplate.opsForList().rightPush(key, value);
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 将list放入缓存
   * @param key   键
   * @param value 值
   * @param time  时间(秒)
   * @return true 成功，false 失败。
   */
  public static boolean setList(String key, Object value, long time) {
    try {
      toolRedis.redisTemplate.opsForList().rightPush(key, value);
      if (time > 0)
        setExpire(key, time);
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 将list放入缓存
   * @param key   键
   * @param value 值
   * @return true 成功，false 失败。
   */
  public static boolean setList(String key, List<Object> value) {
    try {
      toolRedis.redisTemplate.opsForList().rightPushAll(key, value);
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 将list放入缓存
   * @param key   键
   * @param value 值
   * @param time  时间(秒)
   * @return true 成功，false 失败。
   */
  public static boolean setList(String key, List<Object> value, long time) {
    try {
      toolRedis.redisTemplate.opsForList().rightPushAll(key, value);
      if (time > 0)
        setExpire(key, time);
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 根据索引修改list中的某条数据
   * @param key   键
   * @param index 索引
   * @param value 值
   * @return true 成功，false 失败。
   */
  public static boolean setList(String key, long index, Object value) {
    try {

      toolRedis.redisTemplate.opsForList().set(key, index, value);
      return true;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return false;
    }
  }

  /**
   * 获取list缓存的长度。
   * @param key 键。
   * @return 返回长度。
   */
  public static long getListSize(String key) {
    try {
      return toLong(toolRedis.redisTemplate.opsForList().size(key));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return 0;
    }
  }

  /**
   * 移除N个值为value。
   * @param key   键。
   * @param count 移除多少个。
   * @param value 值。
   * @return 移除的个数。
   */
  public static long removeList(String key, long count, Object value) {
    try {
      Long remove = toolRedis.redisTemplate.opsForList().remove(key, count, value);
      return remove == null ? 0 : remove;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
      return 0;
    }
  }

  // >>----------------------- list -----------------------

  // >>----------------------- prefix -----------------------
  
  public static long delByPrefix(final String prefixKey){
    Set<String> keys = toolRedis.redisTemplate.keys(prefixKey);
    if(!CollectionUtils.isEmpty(keys)) {
      Long num = toolRedis.redisTemplate.delete(keys);
      return num == null ? 0 : num;
    }
    return 0;
  }

  // >>----------------------- prefix -----------------------

  // >>----------------------- tool -----------------------

  private static long toLong(Long val) {
    return val == null ? 0 : val;
  }

  private static boolean toBoolean(Boolean val) {
    return val == null ? false : val;
  }

  // >>----------------------- tool -----------------------
}