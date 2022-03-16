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
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolConfigure implements AsyncConfigurer {

  @Value("${thread.pool.executor.corePoolSize:10}")
  private Integer corePoolSize;
  @Value("${thread.pool.executor.maxPoolSize:50}")
  private Integer maxPoolSize;
  @Value("${thread.pool.executor.queueCapacity:100}")
  private Integer queueCapacity;
  @Value("${thread.pool.executor.keepAliveSeconds:300}")
  private Integer keepAliveSeconds;
  @Value("${thread.pool.executor.awaitTerminationSeconds:60}")
  private Integer executorAawaitTerminationSeconds;
  @Value("${thread.pool.executor.threadNamePrefix:taskExecutor-}")
  private String executorThreadNamePrefix;

  @Value("${thread.pool.scheduler.poolSize:300}")
  private Integer poolSize;
  @Value("${thread.pool.scheduler.awaitTerminationSeconds:60}")
  private Integer schedulerAawaitTerminationSeconds;
  @Value("${thread.pool.scheduler.threadNamePrefix:taskScheduler-}")
  private String schedulerThreadNamePrefix;

  @Bean({"localTaskExecutor"})
  public ThreadPoolTaskExecutor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setKeepAliveSeconds(keepAliveSeconds);
    executor.setWaitForTasksToCompleteOnShutdown(true);  //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    executor.setAwaitTerminationSeconds(executorAawaitTerminationSeconds);  //设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，而不是阻塞住
    executor.setThreadNamePrefix(executorThreadNamePrefix);
    executor.setRejectedExecutionHandler(new CallerRunsPolicy());
    executor.initialize();
    return executor;
  }

  @Bean
  public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
    ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
    executor.setPoolSize(poolSize);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(schedulerAawaitTerminationSeconds);
    executor.setThreadNamePrefix(schedulerThreadNamePrefix);
    return executor;
  }

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
}