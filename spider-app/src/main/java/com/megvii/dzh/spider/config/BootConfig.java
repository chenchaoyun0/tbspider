package com.megvii.dzh.spider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.megvii.dzh.perfrom.concurrent.pool.ThreadPool;
import com.megvii.dzh.spider.po.Comment;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.po.User;
import com.megvii.dzh.spider.po.UserTbs;
import com.megvii.dzh.spider.po.WordDivide;
import lombok.Data;

@Component
@Data
public class BootConfig {
  /**
   * 串口名称
   */
  @Value("${server.port}")
  private String serverPort;
  
  @Value("${spider.threads:100}")
  private int spiderThreads;
  
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
  /**
   * 
   */
  @Autowired
  private ThreadPool<Post> threadPoolPost;
  /**
   * 
   */
  @Autowired
  private ThreadPool<WordDivide> threadPoolWordDivide;
  /**
   * 
   */
  @Autowired
  private ThreadPool<Comment> threadCommentDivide;
  /**
   * 
   */
  @Autowired
  private ThreadPool<User> threadUserDivide;
  /**
   * 
   */
  @Autowired
  private ThreadPool<UserTbs> threadUserTbsDivide;
}
