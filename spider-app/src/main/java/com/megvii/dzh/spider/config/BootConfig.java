package com.megvii.dzh.spider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import lombok.Data;

@Component
@Data
public class BootConfig {
  /**
   * 串口名称
   */
  @Value("${server.port}")
  private String serverPort;
  
  /**
   * 重试次数
   */
  @Value("${site.retry.times}")
  private String siteRetryTimes;
  
  /**
   * 抓取间隔
   */
  @Value("${site.sleep.time}")
  private String siteSleepTime;
}
