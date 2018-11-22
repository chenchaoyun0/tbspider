package com.megvii.dzh.spider;

import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.common.utils.LogUtil;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ServletComponentScan
@ComponentScan({"com.megvii"})
@MapperScan("com.megvii.dzh.spider.mapper")
@Slf4j
public class SpiderApplication implements ApplicationListener<ContextRefreshedEvent> {

  public static void main(String args[]) {
    SpringApplication.run(SpiderApplication.class, args);
    log.info(">>>>>>>>>>>>>>>>>>>>>>spiderboot 启动成功!<<<<<<<<<<<<<<<<<<<<<<<<<");
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("--->开机服务执行的操作....");
    try {
      BootConfig bootConfig = SpringUtils.getBean(BootConfig.class);
      Constant.setTbName(bootConfig.getSpiderTbName());
      log.info("---> 待爬取的贴吧名为: {}", Constant.getTbName());
      log.info("输入配置:{}", LogUtil.jsonFormatter(LogUtil.formatLog(bootConfig)));
      DataSource dataSource = SpringUtils.getBean(DataSource.class);
      String datasourceUrl = dataSource.getConnection().getMetaData().getURL();
      String userName = dataSource.getConnection().getMetaData().getUserName();
      log.info("数据源配置:{}@{}", userName, datasourceUrl);
    } catch (Exception e) {
      log.error("onApplicationEvent error", e);
    }
  }

}
