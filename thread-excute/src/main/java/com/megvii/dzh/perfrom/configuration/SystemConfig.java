package com.megvii.dzh.perfrom.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName SystemConfig
 * @Description 系统配置参数类
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午2:36
 */
@Component
public class SystemConfig {

  @Value("${perform_queue_size:5000}")
  public int arrayBlockingQueueSize; // 定义下载队列长度

  @Value("${perform_thread_pool_size:30}")
  public int poolSize; // 线程池大小
}
