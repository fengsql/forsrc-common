package com.forsrc.common.spring.thread;

import com.forsrc.common.spring.db.DbOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ThreadDbOperator implements Runnable {

  @Resource
  private DbOperator<?> dbOperator;

  @Override
  public void run() {
    runOperator();
  }

  private void runOperator() {
    try {
      dbOperator.exec();
    } catch (Exception e) {
      log.error("dbOperator exec error!", e);
    }
  }
}