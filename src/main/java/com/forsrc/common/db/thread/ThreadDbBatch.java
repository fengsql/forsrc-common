package com.forsrc.common.db.thread;

import com.forsrc.common.db.batch.DbBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ThreadDbBatch implements Runnable {

  @Resource
  private ThreadPoolTaskExecutor localTaskExecutor;
  @Resource
  private DbBatch<?> dbBatch;

  @Override
  public void run() {
    runBatch();
  }

  private void runBatch() {
    localTaskExecutor.execute(() -> {
      dbBatch.exec();
    });
  }
}