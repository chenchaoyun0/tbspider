package com.megvii.dzh.spider.webmagic.processors;

import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.spider.config.Constant;
import com.megvii.dzh.spider.po.PostUser;
import com.megvii.dzh.spider.utils.PhantomJsDriverUtils;
import com.megvii.dzh.spider.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.utils.SpiderFileUtils;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

@Slf4j
public class PostDetailProcessor implements PageProcessor {
    /**
     * 匹配每页帖子地址
     */
    private static final String POST_PAGE_URL = "/p/\\d++\\?pn=\\d++";

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
        WebDriver driver=null;
        try {
            String url = page.getRequest().getUrl();
            driver= PhantomJsDriverUtils.getPhantomJSDriver();
            long start = System.currentTimeMillis();
            log.info("---> driver get url {} begin...",url);
            driver.get(url);
            log.info("---> driver get url {} end...cause {} ms",(System.currentTimeMillis()-start));
            String pageSource = driver.getPageSource();
            SpiderFileUtils.writeString2local(pageSource, "E://tieb-spider//2.html");
            Html html = new Html(pageSource);
            
            List<String> list = html.links().regex(POST_PAGE_URL).all();
            
            String data = html.xpath("//*[@id=\"j_p_postlist\"]/div[1]/@data-field").get();
            PostUser postUser=null;
            if(StringUtils.isNotBlank(data)){
                postUser = JSONObject.parseObject(data, PostUser.class);
            }
            String time = html.xpath("//*[@id=\"j_p_postlist\"]/div[1]/div[3]/div[3]/div[1]/ul[2]/li[2]/span/text()").get();
            String content = html.xpath("//*[@id=\"post_content_"+postUser.getContent().getPost_id()+"\"]/text()").get();
            page.putField("time", time==null?postUser.getContent().getDate():time);
            page.putField("content", content);
            page.putField("postUser", postUser);
            
            
        } catch (Exception e) {
            log.error("PostDetailProcessor error {}",e);
        }finally {
            if(driver!=null){
                driver.close();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        //

        ResultItems result = Spider.create(new PostDetailProcessor())//
                .get("http://tieba.baidu.com/p/5414842553");
    }
}
