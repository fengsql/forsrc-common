package com.forsrc.common.scheduler.define;

import com.forsrc.common.scheduler.base.ITask;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Data
@Slf4j
public class SchedulerTask {

  private String name;

  private String cron;

  private Date startTime;

  private ITask task;

  private ScheduledFuture<?> future;

}