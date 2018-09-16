package com.megvii.spider.mine.test;

import java.util.Date;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.megvii.dzh.spider.config.Constant;
import com.megvii.dzh.spider.utils.PhantomJsDriverUtils;
import com.megvii.dzh.spider.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.utils.SpiderFileUtils;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

@Slf4j
public class SeleniumProcessor implements PageProcessor {
    /**
     * 匹配帖子地址
     */
    private static final String POST_URL = "/p/\\d++";
    /**
     * 匹配帖子列表页,${0} 为注入 贴吧名称 如 太原工业学院
     */
    private static final String POST_LIST = "\\/\\/tieba.baidu.com\\/f\\?kw={0}\\&ie=utf-8\\&pn=\\d++";

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数、代理、UserAgent等
    private Site site = Site.me()//
            .addHeader("Proxy-Authorization", ProxyGeneratedUtil.authHeader(Constant.ORDER_NUM, Constant.SECRET, (int) (new Date().getTime() / 1000)))// 设置代理
            .setDisableCookieManagement(true).setCharset("UTF-8")//
            .setTimeOut(60000)//
            .setRetryTimes(10)//
            .setSleepTime(new Random().nextInt(20) * 100)//
            .setUserAgent(
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; Baiduspider-render/2.0; +http://www.baidu.com/search/spider.html)");

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        WebDriver driver= PhantomJsDriverUtils.getPhantomJSDriver();
        String url = page.getRequest().getUrl();
        driver.get(url);
        String pageSource = driver.getPageSource();
        SpiderFileUtils.writeString2local(pageSource, "E://tieb-spider//2.html");
        String title = driver.getTitle();
        WebElement webElement = driver.findElement(By.id("page"));
        String str = webElement.getAttribute("outerHTML");

        Html html = new Html(str);
    }


    public static void main(String[] args) throws Exception {
        //

        Spider.create(new SeleniumProcessor())//
                .addUrl("http://tieba.baidu.com/p/5882089292")//
                .thread(1)//
                .run();
    }
}
