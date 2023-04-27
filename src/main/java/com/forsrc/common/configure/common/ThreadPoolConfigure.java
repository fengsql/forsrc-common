package com.forsrc.common.configure.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;

@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolConfigure implements AsyncConfigurer {

  @Value("${thread.pool.executor.corePoolSize:10}")
  private Integer corePoolSize;
  @Value("${thread.pool.executor.maxPoolSize:100}")
  private Integer maxPoolSize;
  @Value("${thread.pool.executor.queueCapacity:50}")
  private Integer queueCapacity;
  @Value("${thread.pool.executor.maxQueueSize:1000}")
  private Integer maxQueueSize;
  @Value("${thread.pool.executor.keepAliveSeconds:300}")
  private Integer keepAliveSeconds;
  @Value("${thread.pool.executor.awaitTerminationSeconds:60}")
  private Integer executorAawaitTerminationSeconds;
  @Value("${thread.pool.executor.threadNamePrefix:taskExecutor-}")
  private String executorThreadNamePrefix;

  @Value("${thread.pool.scheduler.poolSize:50}")
  private Integer poolSize;
  @Value("${thread.pool.scheduler.maxQueueSize:1000}")
  private Integer schedulerMaxQueueSize;
  @Value("${thread.pool.scheduler.awaitTerminationSeconds:60}")
  private Integer schedulerAawaitTerminationSeconds;
  @Value("${thread.pool.scheduler.threadNamePrefix:taskScheduler-}")
  private String schedulerThreadNamePrefix;

  @Bean({"localThreadPoolTaskExecutor"})
  public ThreadPoolTaskExecutor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setKeepAliveSeconds(keepAliveSeconds);
    executor.setWaitForTasksToCompleteOnShutdown(true);  //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    executor.setAwaitTerminationSeconds(executorAawaitTerminationSeconds);  //设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，而不是阻塞住
    executor.setThreadNamePrefix(executorThreadNamePrefix);
    //        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setRejectedExecutionHandler(rejectedExecutionHandler(maxQueueSize, maxPoolSize));
    executor.initialize();
    log.info("ThreadPoolTaskExecutor initialize ok.");
    return executor;
  }

  @Bean
  public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
    ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
    executor.setPoolSize(poolSize);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(schedulerAawaitTerminationSeconds);
    executor.setThreadNamePrefix(schedulerThreadNamePrefix);
    executor.setRejectedExecutionHandler(rejectedExecutionHandler(schedulerMaxQueueSize, 0));
    return executor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    StringBuilder params = new StringBuilder();
    return (throwable, method, objects) -> {
      Arrays.stream(objects).forEachOrdered((param) -> {
        params.append(param).append(",");
      });
      log.info("Thread Pool Exception message: {}, Method name: {}, bean: {}", //
        new Object[]{throwable.getMessage(), method.getName(), params});
    };
  }

  /**
   * 这个策略就是忽略缓冲队列限制，继续往里边塞，当达到 maxQueueSize 时，抛出异常
   */
  private RejectedExecutionHandler rejectedExecutionHandler(int maxQueueSize, int maxPoolSize) {
    return (runnable, executor) -> {
      BlockingQueue<Runnable> queue = executor.getQueue();
      //      log.info("maxQueueSize: {}. queue: {}", maxQueueSize, queue.size());
      if (maxQueueSize > 0 && queue.size() > maxQueueSize) {
        throw new RejectedExecutionException("Task " + runnable.toString() + " rejected from " + executor.toString());
      }
      try {
        queue.put(runnable);
        int active = executor.getActiveCount();
        long task = executor.getTaskCount();
        long completed = executor.getCompletedTaskCount();
        int size = queue.size();
        if (maxPoolSize > 0 && active >= maxPoolSize) {
          log.info("put ok. active: {}. queue: {}. max: {}. task: {}", active, size, maxQueueSize, task);
        }
      } catch (InterruptedException e) {
        log.error("put task into queue error!", e);
      }
    };
  }
}