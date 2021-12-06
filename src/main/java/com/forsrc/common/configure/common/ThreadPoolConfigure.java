package com.forsrc.common.configure.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolConfigure implements AsyncConfigurer {

  @Value("${thread.pool.corePoolSize:10}")
  private Integer corePoolSize;

  @Value("${thread.pool.maxPoolSize:50}")
  private Integer maxPoolSize;

  @Value("${thread.pool.queueCapacity:100}")
  private Integer queueCapacity;

  @Value("${thread.pool.keepAliveSeconds:300}")
  private Integer keepAliveSeconds;

  @Bean({"localTaskExecutor"})
  public ThreadPoolTaskExecutor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setKeepAliveSeconds(keepAliveSeconds);
        /*executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(900);*/
    executor.setThreadNamePrefix("LocalAsync-");
    executor.setRejectedExecutionHandler(new CallerRunsPolicy());
    executor.initialize();
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