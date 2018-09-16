package com.megvii.dzh.spider;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import com.megvii.dzh.spider.utils.CrowProxyProvider;
import com.megvii.dzh.spider.utils.SpringUtils;
import com.megvii.dzh.spider.webmagic.pipelines.PostDownloadPipeline;
import com.megvii.dzh.spider.webmagic.processors.PostProcessor;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.spring.annotation.MapperScan;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;

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
            List<String> urls = new ArrayList<>();
            for (int i = 0; i < 100000; i = i + 50) {
                String url = "http://tieba.baidu.com/f?kw=太原工业学院&ie=utf-8&pn=" + i;
                urls.add(url);
            }

            HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
            // 设置动态转发代理，使用定制的ProxyProvider
            httpClientDownloader.setProxyProvider(CrowProxyProvider.from(new Proxy("forward.xdaili.cn", 80)));
            Spider.create(new PostProcessor())//
//                    .addUrl(urls.toArray(new String[]{}))
                    .addUrl("http://tieba.baidu.com/f?kw=太原工业学院&ie=utf-8&pn=0")//
                    .addPipeline(SpringUtils.getBean(PostDownloadPipeline.class))//
                    .thread(20)//
                    .runAsync();
        } catch (Exception e) {
            log.error("onApplicationEvent error", e);
        }
    }

}
