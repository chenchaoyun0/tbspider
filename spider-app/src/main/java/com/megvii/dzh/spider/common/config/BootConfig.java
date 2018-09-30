package com.megvii.dzh.spider.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@Data
public class BootConfig {

  @Value("${server.port}")
  private String serverPort;
  /**
   * 爬取的线程数
   */
  @Value("${spider.threads:100}")
  private int spiderThreads;
  /**
   * 爬取的贴吧名，没有配置则获取constant 里的名字
   */
  @Value("${spider.tb.name}")
  private String spiderTbName;

  /**
   * 重试次数
   */
  @Value("${site.retry.times:4}")
  private String siteRetryTimes;
  
  /**
   * 抓取间隔
   */
  @Value("${site.sleep.time}")
  private String siteSleepTime;
}
