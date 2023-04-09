package com.forsrc.common.spring.db;

import com.forsrc.common.constant.ConstDB;
import com.forsrc.common.spring.base.IDao;
import com.forsrc.common.spring.base.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class DbOperator<T> {

  private List<DbEntity<T>> entitys = Collections.synchronizedList(new LinkedList<>());

  @Resource
  private DbBatch dbBatch;

  //<<---------------------------------------- initialize ----------------------------------------
  public DbOperator() {

  }
  //>>---------------------------------------- initialize ----------------------------------------

  //<<---------------------------------------- public ----------------------------------------

  //<<<---------------------------------------- dao ----------------------------------------

  public void insert(T t, IDao<T> dao) {
    DbEntity<T> dbEntity = new DbEntity<>();
    dbEntity.setVo(t);
    dbEntity.setDao(dao);
    dbEntity.setLayerType(ConstDB.layerType.dao);
    dbEntity.setSqlType(ConstDB.sqlType.insert);
    add(dbEntity);
  }

  public void update(T t, IDao<T> dao) {
    DbEntity<T> dbEntity = new DbEntity<>();
    dbEntity.setVo(t);
    dbEntity.setDao(dao);
    dbEntity.setLayerType(ConstDB.layerType.dao);
    dbEntity.setSqlType(ConstDB.sqlType.update);
    add(dbEntity);
  }

  public void delete(T t, IDao<T> dao) {
    DbEntity<T> dbEntity = new DbEntity<>();
    dbEntity.setVo(t);
    dbEntity.setDao(dao);
    dbEntity.setLayerType(ConstDB.layerType.dao);
    dbEntity.setSqlType(ConstDB.sqlType.delete);
    add(dbEntity);
  }

  //>>>---------------------------------------- dao ----------------------------------------

  //<<<---------------------------------------- service ----------------------------------------

  public void insert(T t, IService<T> service) {
    DbEntity<T> dbEntity = new DbEntity<>();
    dbEntity.setVo(t);
    dbEntity.setService(service);
    dbEntity.setLayerType(ConstDB.layerType.service);
    dbEntity.setSqlType(ConstDB.sqlType.insert);
    add(dbEntity);
  }

  public void update(T t, IService<T> service) {
    DbEntity<T> dbEntity = new DbEntity<>();
    dbEntity.setVo(t);
    dbEntity.setService(service);
    dbEntity.setLayerType(ConstDB.layerType.service);
    dbEntity.setSqlType(ConstDB.sqlType.update);
    add(dbEntity);
  }

  public void delete(T t, IService<T> service) {
    DbEntity<T> dbEntity = new DbEntity<>();
    dbEntity.setVo(t);
    dbEntity.setService(service);
    dbEntity.setLayerType(ConstDB.layerType.service);
    dbEntity.setSqlType(ConstDB.sqlType.delete);
    add(dbEntity);
  }

  public int size() {
    return entitys.size();
  }

  //>>>---------------------------------------- service ----------------------------------------

  //<<<---------------------------------------- normal ----------------------------------------

  /**
   * 提交数据。
   * @param maxRow 提交最大记录数，0 全部提交。
   * @return 提交的记录数。
   */
  public int exec(int maxRow) {
    return save(maxRow);
  }

  public int exec() {
//    log.info("exec");
    return save(0);
  }

  //>>>---------------------------------------- normal ----------------------------------------

  //>>---------------------------------------- public ----------------------------------------

  //<<---------------------------------------- protected ----------------------------------------

  //>>---------------------------------------- protected ----------------------------------------

  //<<---------------------------------------- private ----------------------------------------

  //<<<---------------------------------------- inner ----------------------------------------

  private void add(DbEntity<T> dbEntity) {
    entitys.add(dbEntity);
    //    log.info("add dbEntity ok. size: " + entitys.size());
  }

  private int save(int maxRow) {
    int size = entitys.size();
    if (entitys.size() <= 0) {
      return 0;
    }
    int row = dbBatch.runBatch(entitys, maxRow);
    if (row <= 0) {
      log.warn("runBatch fail! size: " + size);
    }
    return row;
  }

  //>>>---------------------------------------- inner ----------------------------------------

  //<<<---------------------------------------- tool ----------------------------------------

  //>>>---------------------------------------- tool ----------------------------------------

  //>>---------------------------------------- private ----------------------------------------

  //<<---------------------------------------- get ----------------------------------------

  //>>---------------------------------------- get ----------------------------------------

  //<<---------------------------------------- set ----------------------------------------

  //>>---------------------------------------- set ----------------------------------------

  //<<---------------------------------------- get set ----------------------------------------

  //>>---------------------------------------- get set ----------------------------------------

}
