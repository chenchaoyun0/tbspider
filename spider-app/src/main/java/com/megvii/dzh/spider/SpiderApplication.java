package com.megvii.dzh.spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import com.megvii.dzh.spider.utils.CrowProxyProvider;
import com.megvii.dzh.spider.utils.SpringUtils;
import com.megvii.dzh.spider.webmagic.pipelines.BookDownloadPipeline;
import com.megvii.dzh.spider.webmagic.processors.BookPageProcessor;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;

@SpringBootApplication
@ServletComponentScan
@ComponentScan({"com.megvii"})
@Slf4j
public class SpiderApplication extends SpringBootServletInitializer
  implements ApplicationListener<ContextRefreshedEvent> {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(SpiderApplication.class);
  }

  public static void main(String args[]) {
    SpringApplication.run(SpiderApplication.class, args);
    log.info(">>>>>>>>>>>>>>>>>>>>>>spiderboot 启动成功!<<<<<<<<<<<<<<<<<<<<<<<<<");
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("--->开机服务执行的操作....");
    try {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        // 设置动态转发代理，使用定制的ProxyProvider
        httpClientDownloader.setProxyProvider(CrowProxyProvider.from(new Proxy("forward.xdaili.cn", 80)));
        //
        BookDownloadPipeline bookDownloadPipeline = SpringUtils.getBean(BookDownloadPipeline.class);
        Spider.create(new BookPageProcessor())//
                .addUrl("http://www.shopbop.ink/bookmanager/book/selectBookPages")//
                .addPipeline(bookDownloadPipeline)//
                .thread(1)//
                .runAsync();
    } catch (Exception e) {
      log.error("onApplicationEvent error", e);
    }
  }

}
