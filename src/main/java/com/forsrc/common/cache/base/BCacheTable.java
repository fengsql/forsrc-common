package com.forsrc.common.cache.base;

import com.forsrc.common.spring.base.IDao;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolJson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BCacheTable<T> extends BCache {

  private String keyName;

  private Class<T> clazz;

  private IDao<T> dao;

  protected abstract String getPrimaryId(T t);

  protected abstract void setPrimaryId(T t, String id);

  protected abstract void putIndex(T t);

  protected abstract void updateIndex(T now, T old);

  protected abstract void deleteIndex(String id);

  protected abstract void refreshIndex(T t);

  public void initialize(String keyName, Class<T> clazz, IDao<T> dao) {
    super.initialize(keyName);
    this.keyName = keyName;
    this.clazz = clazz;
    this.dao = dao;
  }

  // <<----------------------- protected -----------------------

  // <<<----------------------- normal -----------------------

  protected T getTable(String id) {
    T t = getCache(id);
    if (t != null) {
      return t;
    }
    t = select(id);
    if (t == null) {
      return null;
    }
    setCache(t);
    putIndex(t);
    return t;
  }

  protected T getCache(String id) {
    String key = getKey(keyName, id);
    return getTableByKey(key);
  }

  protected T getTableByKey(String key) {
    String json = getRedis(key);
    if (Tool.isNull(json)) {
      return null;
    }
    T t = ToolJson.toBean(json, clazz);
    refreshExpire(key);
    refreshIndex(t);
    return t;
  }

  protected void putCache(T t) {
    setCache(t);
  }

  protected boolean saveTable(T t) {
    if (t == null) {
      log.warn("t is null!");
      return false;
    }
    if (getPrimaryId(t) != null) {
      return mergeTable(t);
    }
    if (!insert(t)) {
      log.warn("insert fail! t: " + ToolJson.toJson(t));
      return false;
    }
    setCache(t);
    putIndex(t);
    return true;
  }

  protected boolean updateCache(T t) {
    if (t == null) {
      log.warn("t is null!");
      return false;
    }
    if (getPrimaryId(t) == null) {
      log.warn("update fail! primaryId is null.");
      return false;
    }
    T old = getCache(getPrimaryId(t));
    if (old != null) {
      updateCache(t, old);
      return true;
    }
    setCache(t);
    return true;
  }

  protected boolean deleteTable(String id) {
    T t = create(id);
    if (!delete(t)) {
      log.warn("delete fail! t: " + ToolJson.toJson(t));
//      return false;
    }
    deleteValue(id);
    return true;
  }

  protected void removeTable(String id) {
    deleteValue(id);
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- protected -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  private boolean mergeTable(T t) {
    if (t == null) {
      log.warn("t is null!");
      return false;
    }
    if (getPrimaryId(t) == null) {
      log.warn("update fail! primaryId is null.");
      return false;
    }
    T old = getCache(getPrimaryId(t));
    if (old != null) {
      if (!update(t)) {
        log.warn("update fail! t: " + ToolJson.toJson(t));
        return false;
      }
      updateCache(t, old);
      return true;
    }
    old = select(getPrimaryId(t));
    if (old == null) {
      if (!insert(t)) {
        log.warn("insert fail! t: " + ToolJson.toJson(t));
        return false;
      }
    } else {
      if (!update(t)) {
        log.warn("update fail! t: " + ToolJson.toJson(t));
        return false;
      }
    }
    setCache(t);
    return true;
  }

  private void updateCache(T t, T old) {
    if (t == null) {
      log.warn("t is null!");
      return;
    }
    updateIndex(t, old);
    String key = getKey(keyName, getPrimaryId(t));
    setRedis(key, ToolJson.toJson(t));
  }

  private void deleteValue(String id) {
    deleteIndex(id);
    String key = getKey(keyName, id);
    delRedis(key);
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- action -----------------------

  private T create(String id) {
    T t = newInstance();
    setPrimaryId(t, id);
    return t;
  }

  private boolean insert(T t) {
    return dao.insert(t) > 0;
  }

  private boolean update(T t) {
    return dao.update(t) > 0;
  }

  private T select(String id) {
    T t = create(id);
    return dao.selectOne(t);
  }

  private boolean delete(T t) {
    return dao.delete(t) >= 0;
  }

  // >>>----------------------- action -----------------------

  // <<<----------------------- tool -----------------------

  private void setCache(T t) {
    if (t == null) {
      log.warn("t is null!");
      return;
    }
    String key = getKey(keyName, getPrimaryId(t));
    setRedis(key, ToolJson.toJson(t));
  }

  @SneakyThrows
  private T newInstance() {
    return clazz.newInstance();
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}