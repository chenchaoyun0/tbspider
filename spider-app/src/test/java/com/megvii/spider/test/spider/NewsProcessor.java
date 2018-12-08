package com.megvii.spider.test.spider;

import com.megvii.dzh.spider.common.utils.SpiderFileUtils;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

@Slf4j
public class NewsProcessor implements PageProcessor {

  //设置agent,伪装成是电脑访问，若不设置，则会是httpclient之类的，有些网站会禁止此类访问
  private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:24.0) Gecko/20100101";


  // 抓取网站的相关配置，包括编码、抓取间隔、重试次数、代理、UserAgent等
  private Site site = Site.me()//
      .setDisableCookieManagement(true).setCharset("GBK")//
      .setTimeOut(60000)//超时
      .setRetryTimes(10)//超时重试
      .setSleepTime(new Random().nextInt(20) * 100)//等待时间
      .setUserAgent(userAgent);//agent

  @Override
  public Site getSite() {
    return site;
  }

  @Override
  public void process(Page page) {
    Html html = page.getHtml();//获取页面
    SpiderFileUtils.writeString2local(html.toString(), "E://news.html");
    List<String> newsUrlList = html.xpath("//*[@id=\"lf_viewer\"]/div[2]/div[1]/ul/li/a/@href").all();
    log.info("newsList size:{}",newsUrlList.size());
    for (String url : newsUrlList) {



    }
  }


  public static void main(String[] args) {
    Spider.create(new NewsProcessor())//
        .addPipeline(new ConsolePipeline())//添加管道
        .addUrl("http://tech.163.com/")//爬取的初始URL
        .thread(1)//爬取线程
        .run();
  }
}
