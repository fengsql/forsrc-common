package com.forsrc.common.db.run;

import com.forsrc.common.constant.ConstDB;
import com.forsrc.common.db.define.DbEntity;
import com.forsrc.common.spring.base.IDao;
import com.forsrc.common.spring.base.IService;
import com.forsrc.common.tool.ToolJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DbRun<T> {
  private static final int default_batchRow = 500;//每批commit的个数
  private static final int max_retry_times = -1;//重试次数，总次数，包含首次
  private static final int retry_second = 10;//每次重试的间隔时间，s

  /**
   * 批量插入并移除。
   * @param entitys 插入的数据，插入后将数据移除。
   * @param maxRow  最大插入记录数，0全部，否则仅插入此记录后退出。
   * @return 返回插入的记录数。
   */
  public int runBatch(List<DbEntity<T>> entitys, int maxRow) {
    int count = 0;
    int size = entitys.size();
    int success = 0;
    int fail = 0;
    while (count < size) {
      DbEntity<T> dbEntity = remove(entitys);
      if (dbEntity == null) {
        break;
      }
      if (!checkRun(entitys, dbEntity)) {
        continue;
      }
      boolean ok = runEntity(entitys, dbEntity);
      if (ok) {
        success++;
      } else {
        fail++;
      }
      count++;
      if (maxRow > 0 && count >= maxRow) {
        break;
      }
    }
    if (fail > 0) {
      log.info("runBatch ok. size: {}. success: {}. fail: {}", size, success, fail);
    } else {
      log.info("runBatch ok. size: {}. success: {}", size, success);
    }
    return count;
  }

  private boolean runEntity(List<DbEntity<T>> entitys, DbEntity<T> dbEntity) {
    boolean retry = false;
    boolean ok = false;
    try {
      int layerType = dbEntity.getLayerType();
      switch (layerType) {
        case ConstDB.layerType.dao:
          runDaoEntity(dbEntity);
          break;
        case ConstDB.layerType.service:
          runServiceEntity(dbEntity);
          break;
        default:
          log.warn("runEntity unknow layerType! layerType: " + layerType);
          break;
      }
      ok = true;
    } catch (DataAccessException e) {
      log.error("runEntity DataAccessException!", e);
      retry = false;
    } catch (Exception e) {
      log.error("runEntity error!", e);
      retry = true;
    }
    if (!retry) {
      retry(entitys, dbEntity);
    }
    return ok;
  }

  //<<---------------------------------------- dao ----------------------------------------

  private void runDaoEntity(DbEntity<T> dbEntity) {
    int sqlType = dbEntity.getSqlType();
    switch (sqlType) {
      case ConstDB.sqlType.insert:
        runDaoInsert(dbEntity);
        break;
      case ConstDB.sqlType.update:
        runDaoUpdate(dbEntity);
        break;
      case ConstDB.sqlType.delete:
        runDaoDelete(dbEntity);
        break;
      default:
        log.warn("runEntityDao unknow sqlType! sqlType: " + sqlType);
        break;
    }
  }

  private void runDaoInsert(DbEntity<T> dbEntity) {
    IDao<T> dao = dbEntity.getDao();
    T t = dbEntity.getVo();
    int count = dao.insert(t);
    if (count <= 0) {
      log.warn("runDaoInsert fail! vo: " + ToolJson.toJson(t));
    }
  }

  private void runDaoUpdate(DbEntity<T> dbEntity) {
    IDao<T> dao = dbEntity.getDao();
    T t = dbEntity.getVo();
    int count = dao.update(t);
    if (count <= 0) {
      log.warn("runDaoUpdate fail! vo: " + ToolJson.toJson(t));
    }
  }

  private void runDaoDelete(DbEntity<T> dbEntity) {
    IDao<T> dao = dbEntity.getDao();
    T t = dbEntity.getVo();
    int count = dao.delete(t);
    if (count <= 0) {
      log.warn("runDaoDelete fail! vo: " + ToolJson.toJson(t));
    }
  }

  //>>---------------------------------------- dao ----------------------------------------

  //<<---------------------------------------- service ----------------------------------------

  private void runServiceEntity(DbEntity<T> dbEntity) {
    if (dbEntity == null) {
      return;
    }
    int sqlType = dbEntity.getSqlType();
    switch (sqlType) {
      case ConstDB.sqlType.insert:
        runServiceInsert(dbEntity);
        break;
      case ConstDB.sqlType.update:
        runServiceUpdate(dbEntity);
        break;
      case ConstDB.sqlType.delete:
        runServiceDelete(dbEntity);
        break;
      default:
        log.warn("runEntityService unknow sqlType! sqlType: " + sqlType);
        break;
    }
  }

  private void runServiceInsert(DbEntity<T> dbEntity) {
    IService<T> service = dbEntity.getService();
    T t = dbEntity.getVo();
    T result = service.insert(null, null, t);
    if (result == null) {
      log.warn("runServiceInsert fail! vo: " + ToolJson.toJson(t));
    }
  }

  private void runServiceUpdate(DbEntity<T> dbEntity) {
    IService<T> service = dbEntity.getService();
    T t = dbEntity.getVo();
    int count = service.update(null, null, t);
    if (count <= 0) {
      log.warn("runServiceUpdate fail! vo: " + ToolJson.toJson(t));
    }
  }

  private void runServiceDelete(DbEntity<T> dbEntity) {
    IService<T> service = dbEntity.getService();
    T t = dbEntity.getVo();
    int count = service.delete(null, null, t);
    if (count <= 0) {
      log.warn("runServiceDelete fail! vo: " + ToolJson.toJson(t));
    }
  }

  //>>---------------------------------------- service ----------------------------------------

  private DbEntity<T> remove(List<DbEntity<T>> entitys) {
    try {
      return entitys.remove(0);
    } catch (Exception e) {
      return null;
    }
  }

  private boolean checkRun(List<DbEntity<T>> entitys, DbEntity<T> dbEntity) {
    int times = dbEntity.getTimes();
    if (times <= 0) {
      return true;
    }
    if (times >= max_retry_times) {
      return false;
    }
    if (!isTimeRun(dbEntity)) {
      add(entitys, dbEntity);
      return false;
    }
    return true;
  }

  private boolean isTimeRun(DbEntity<T> dbEntity) {
    long now = System.currentTimeMillis();
    long lastTime = dbEntity.getLastTime();
    int second = (int) ((now - lastTime) / 1000);
    return second >= retry_second;
  }

  private void retry(List<DbEntity<T>> entitys, DbEntity<T> dbEntity) {
    if (max_retry_times < 0) {
      return;
    }
    int times = dbEntity.getTimes();
    times++;
    if (times >= max_retry_times) {
      log.warn("retry max times! entity: " + ToolJson.toJson(dbEntity.getVo()));
      return;
    }
    dbEntity.setTimes(times);
    dbEntity.setLastTime(System.currentTimeMillis());
    add(entitys, dbEntity);
  }

  private void add(List<DbEntity<T>> entitys, DbEntity<T> dbEntity) {
    entitys.add(dbEntity);
  }

}
