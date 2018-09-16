package com.megvii.dzh.spider.webmagic.processors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.spider.config.Constant;
import com.megvii.dzh.spider.po.Comment;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.po.PostUser;
import com.megvii.dzh.spider.utils.CrowProxyProvider;
import com.megvii.dzh.spider.utils.DateConvertUtils;
import com.megvii.dzh.spider.utils.PhantomJsDriverUtils;
import com.megvii.dzh.spider.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.utils.SpiderFileUtils;
import com.megvii.dzh.spider.utils.URLGeneratedUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.Html;

@Slf4j
public class PostSeleniumProcessor implements PageProcessor {
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

        try {
            String url = page.getRequest().getUrl();
            WebDriver driver = PhantomJsDriverUtils.getPhantomJSDriver();
            long start = System.currentTimeMillis();
            log.info("---> driver get url {} begin...", url);
            driver.get(url);
            log.info("---> driver get url {} end...cause {} ms", (System.currentTimeMillis() - start));
            String pageSource = driver.getPageSource();
            driver.close();
            Html html = new Html(pageSource);
            SpiderFileUtils.writeString2local(pageSource, "E://tieb-spider//2.html");
            //
            List<String> listPosts = html.links().regex(POST_URL).all();
            listPosts.forEach(e -> URLGeneratedUtil.generatePostURL(e));
            page.addTargetRequests(listPosts);

            /**
             * 帖子页
             */
            if (page.getUrl().regex(POST_URL).match()) {
                crawlPost(page, html);
            }


        } catch (Exception e) {
            log.error("PostDetailProcessor error {}", e);
        } finally {
        }

    }


    /**
     * 帖子页 获取帖子数据
     * 
     * @param page
     */
    private void crawlPost(Page page, Html html) {
        try {
            
            //查看该帖子有多少页
            String pageSize = html.xpath("//*[@id=\"thread_theme_5\"]/div[1]/ul/li[2]/span[2]/text()").get();
            //将帖子的下一页加入待爬
            int size = Integer.parseInt(pageSize);
            if(size>2){
                for (int i = 2; i <size; i++) {
                    String url=page.getUrl().toString()+"?pn="+i;
                    page.addTargetRequest(url);
                }
            }
            /**
             * 主题信息
             */
            Post post = new Post();
            String data = html.xpath("//*[@id=\"j_p_postlist\"]/div[1]/@data-field").get();
            PostUser postUser = null;
            if (StringUtils.isNotBlank(data)) {
                postUser = JSONObject.parseObject(data, PostUser.class);
            }
            String time = html.xpath("//*[@id=\"j_p_postlist\"]/div[1]/div[3]/div[3]/div[1]/ul[2]/li[2]/span/text()").get();
            String content = html.xpath("//*[@id=\"post_content_" + postUser.getContent().getPost_id() + "\"]/text()").get();
            String replyNum = html.xpath("//*[@id=\"thread_theme_5\"]/div[1]/ul/li[2]/span[1]/text()").get();
            String title = html.xpath("//*[@id=\"j_core_title_wrap\"]/div[2]/h1/text()").get();
            String userName = html.xpath("//*[@id=\"j_p_postlist\"]/div[1]/div[2]/ul/li[3]/a/text()").get();
            //
            post.setContent(content);
            post.setPostUrl(page.getUrl().toString());
            post.setReplyNum(Integer.parseInt(replyNum));
            post.setTime(DateConvertUtils.parse(time == null ? postUser.getContent().getDate() : time, DateConvertUtils.DATE_TIME_NO_SS));
            post.setTitle(title);
            post.setType(1);
            post.setUserName(userName);
            //
            page.putField("post", post);
            /**
             * 回复信息
             */
            commentData(page, html);
            
        } catch (Exception e) {
            log.error("crawlPost error {}", e);
        }
    }


    private void commentData(Page page, Html html) {
        /**
         * 回复信息
         */
        List<Comment> listComment=new ArrayList<>();
        for (int i = 2; i <=50; i++) {
            String dataComment = html.xpath("//*[@id=\"j_p_postlist\"]/div["+i+"]/@data-field").get();
            PostUser dataCommentPo=null;
            if (StringUtils.isNotBlank(dataComment)) {
                Comment comment = new Comment();
                dataCommentPo = JSONObject.parseObject(dataComment, PostUser.class);
                //
                String contentComment = html.xpath("//*[@id=\"post_content_"+dataCommentPo.getContent().getPost_id()+"\"]/text()").get();
                String userNameComment = html.xpath("//*[@id=\"j_p_postlist\"]/div["+i+"]/div[2]/ul/li[3]/a/text()").get();
                //
                comment.setContent(contentComment);
                comment.setPostUrl(page.getUrl().toString());
                comment.setUserDevice(dataCommentPo.getContent().getOpen_type());
                comment.setTime(DateConvertUtils.parse(dataCommentPo.getContent().getDate(), DateConvertUtils.DATE_TIME_NO_SS));
                comment.setUserName(userNameComment);
                //
                listComment.add(comment);
            }else{
                continue;
            }
        }
        page.putField("listComment", listComment);
    }

    public static void main(String[] args) {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        // 设置动态转发代理，使用定制的ProxyProvider
        httpClientDownloader.setProxyProvider(CrowProxyProvider.from(new Proxy("forward.xdaili.cn", 80)));
        Spider.create(new PostSeleniumProcessor())//
                .addUrl("http://tieba.baidu.com/f?kw=%E5%A4%AA%E5%8E%9F%E5%B7%A5%E4%B8%9A%E5%AD%A6%E9%99%A2&ie=utf-8&pn=0")//
                .addPipeline(new ConsolePipeline())//
                .thread(1)//
                .run();
    }
}
