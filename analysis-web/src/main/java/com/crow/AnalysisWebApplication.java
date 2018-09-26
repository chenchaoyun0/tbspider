package com.crow;

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
@ComponentScan({"com.crow"})
@MapperScan("com.crow.domain")
@Slf4j
public class AnalysisWebApplication extends SpringBootServletInitializer implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String args[]) {
        SpringApplication.run(AnalysisWebApplication.class, args);
        log.info(">>>>>>>>>>>>>>>>>>>>>>analysis-web 启动成功!<<<<<<<<<<<<<<<<<<<<<<<<<");
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
