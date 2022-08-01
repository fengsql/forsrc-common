package com.forsrc.common.scheduler.manage;

import com.forsrc.common.scheduler.base.ITask;
import com.forsrc.common.scheduler.data.DataScheduler;
import com.forsrc.common.scheduler.define.SchedulerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class ManageScheduler {

  @Resource
  private ThreadPoolTaskScheduler threadPoolTaskScheduler;
  @Resource
  private DataScheduler dataScheduler;

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public SchedulerTask add(ITask task, String cron) {
    String taskName = task.getName();
    SchedulerTask schedulerTask = getSchedulerTask(taskName);
    if (schedulerTask != null) {
      log.warn("task exists! name: {}", taskName);
      return null;
    }
    schedulerTask = addTask(task, cron);
    log.info("add task ok. name: {}", taskName);
    return schedulerTask;
  }

  public void stop(ITask task) {
    String taskName = task.getName();
    SchedulerTask schedulerTask = getSchedulerTask(taskName);
    if (schedulerTask == null) {
      log.warn("task not exists! name: {}", taskName);
      return;
    }
    boolean ok = schedulerTask.getFuture().cancel(true);
    if (!ok) {
      log.warn("stop task fail! name: {}", taskName);
      return;
    }
    removeSchedulerTask(schedulerTask);
    log.info("stop task ok. name: {}", taskName);
  }

  public SchedulerTask restart(ITask task, String cron) {
    stop(task);
    return add(task, cron);
  }

  public boolean isCancelled(ITask task) {
    String taskName = task.getName();
    SchedulerTask schedulerTask = getSchedulerTask(taskName);
    if (schedulerTask == null) {
      log.warn("task not exists! name: {}", taskName);
      return false;
    }
    return schedulerTask.getFuture().isCancelled();
  }

  public boolean isDone(ITask task) {
    String taskName = task.getName();
    SchedulerTask schedulerTask = getSchedulerTask(taskName);
    if (schedulerTask == null) {
      log.warn("task not exists! name: {}", taskName);
      return false;
    }
    return schedulerTask.getFuture().isDone();
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  private SchedulerTask addTask(ITask task, String cron) {
    Trigger trigger = getTrigger(cron);
    ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(task, trigger);
    SchedulerTask schedulerTask = newSchedulerTask(task, cron, future);
    addSchedulerTask(schedulerTask);
    return schedulerTask;
  }

  private SchedulerTask getSchedulerTask(String taskName) {
    return dataScheduler.getTasks().get(taskName);
  }

  private SchedulerTask newSchedulerTask(ITask task, String cron, ScheduledFuture<?> future) {
    SchedulerTask schedulerTask = new SchedulerTask();
    schedulerTask.setName(task.getName());
    schedulerTask.setCron(cron);
    schedulerTask.setTask(task);
    schedulerTask.setFuture(future);
    schedulerTask.setStartTime(new Date());
    return schedulerTask;
  }

  private void addSchedulerTask(SchedulerTask schedulerTask) {
    String name = schedulerTask.getName();
    dataScheduler.getTasks().put(name, schedulerTask);
  }

  private void removeSchedulerTask(SchedulerTask schedulerTask) {
    String name = schedulerTask.getName();
    dataScheduler.getTasks().remove(name);
  }

  private Trigger getTrigger(String cron) {
    return new CronTrigger(cron);
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}