package com.forsrc.common.scheduler.data;

import com.forsrc.common.scheduler.define.SchedulerTask;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Data
public class DataScheduler {

  private Map<String, SchedulerTask> tasks = new ConcurrentHashMap<>();

}