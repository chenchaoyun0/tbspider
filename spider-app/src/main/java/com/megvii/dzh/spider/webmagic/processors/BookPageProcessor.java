package com.megvii.dzh.spider.webmagic.processors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import com.megvii.dzh.spider.config.Constant;
import com.megvii.dzh.spider.po.BookPo;
import com.megvii.dzh.spider.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.utils.URLGeneratedUtil;
import com.megvii.dzh.spider.utils.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
@Slf4j
public class BookPageProcessor implements PageProcessor {

    private static final String ONE_PAGE = "/bookmanager/book/selectBookPages\\?pageNo=\\d++\\&";

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数、代理、UserAgent等
    private Site site = Site.me()//
            .addHeader("Proxy-Authorization", ProxyGeneratedUtil.authHeader(Constant.ORDER_NUM, Constant.SECRET, (int) (new Date().getTime() / 1000)))// 设置代理
            .setDisableCookieManagement(true).setCharset("UTF-8")//
            .setTimeOut(60000)//
            .setRetryTimes(10)//
            .setSleepTime(new Random().nextInt(20) * 100)//
            .setUserAgent(UserAgentUtil.getRandomUserAgent());

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        // 获取所有页
        List<String> listUrls = page.getHtml().links().regex(ONE_PAGE).all();
        listUrls.forEach(e -> URLGeneratedUtil.generatePostURL(e));
        page.addTargetRequests(listUrls);
        //获取数据页
        if (page.getUrl().regex(ONE_PAGE).match()) {
            crawlBook(page);
        }
    }

    private void crawlBook(Page page) {
        ///html/body/div[9]/div/div[1]/div/div[1]/a/img
        ///html/body/div[9]/div/div[2]/div/div[1]/a/img
        log.info("---> 当前请求页面 {}",page.getUrl());
        List<BookPo> listBookPo=new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            String photoUrl = page.getHtml().xpath("/html/body/div[9]/div/div[" + i + "]/div/div[1]/a/").$("img", "src").get();
            System.out.println("photoUrl-------> " + photoUrl);
            String bookName = page.getHtml().xpath("/html/body/div[9]/div/div[" + i + "]/div/h2/a[1]/text()").toString();
            System.out.println("bookName-------> " + bookName);
            BookPo bookPo = new BookPo();
            bookPo.setBookName(bookName);
            bookPo.setPhotoUrl(photoUrl);
            listBookPo.add(bookPo);
        }
        page.putField("listBookPo", listBookPo);

    }
}
