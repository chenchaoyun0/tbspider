package com.megvii.dzh.spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ServletComponentScan
@ComponentScan({"com.megvii"})
@MapperScan("com.megvii.dzh.spider.mapper")
@Slf4j
public class SpiderApplication extends SpringBootServletInitializer implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String args[]) {
        SpringApplication.run(SpiderApplication.class, args);
        log.info(">>>>>>>>>>>>>>>>>>>>>>spiderboot 启动成功!<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("--->开机服务执行的操作....");
        try {
            
        } catch (Exception e) {
            log.error("onApplicationEvent error", e);
        }
    }

}
