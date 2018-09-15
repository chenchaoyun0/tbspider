package com.megvii.dzh.spider.webmagic.processors;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import com.megvii.dzh.spider.config.Constant;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.utils.CrowProxyProvider;
import com.megvii.dzh.spider.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.utils.URLGeneratedUtil;
import com.megvii.dzh.spider.webmagic.pipelines.PostDownloadPipeline;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.Selectable;

@Slf4j
public class PostProcessor implements PageProcessor {
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
        Selectable url = page.getUrl();
        log.info("---> url {}", url);
        /**
         * 将所有帖子存入数据库待爬
         */
        //SpiderFileUtils.writeString2local(page.getHtml().toString(), "E://tieb-spider//2.txt");
        List<String> listPosts = page.getHtml().links().regex(POST_URL).all();
        //listPosts.forEach(e -> URLGeneratedUtil.generatePostURL(e));
        // page.addTargetRequests(listPosts);
        page.putField("listPosts", listPosts);

        /**
         * 帖子分页页面
         */
        String generatePostURL = URLGeneratedUtil.generateHttpURL(MessageFormat.format(POST_LIST, Constant.TIEBA_NAME));
        if (page.getUrl().regex(generatePostURL).match()) {
            crawlPost(page);
        }

    }


    /**
     * 帖子页 获取帖子数据
     * 
     * @param page
     */
    private void crawlPost(Page page) {
        //SpiderFileUtils.writeString2local(page.getHtml().toString(), "E://tieb-spider//2.html");
        try {
            List<Post> postList = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {
                // 置顶帖过滤
                String cla = page.getHtml().xpath("//*[@id=\"frslistcontent\"]/li[" + i + "]").$("a", "class").get();
                if ("j_common ti_item no_border".equals(cla)) {
                    continue;
                }

                Post post = new Post();
                String title = page.getHtml().xpath("//*[@id=\"frslistcontent\"]/li[" + i + "]/a/div[1]/span/text()").toString();
                Integer type=1;
                // 精华帖
                if ("精".equals(title)) {
                    type=2;
                    title =page.getHtml().xpath("//*[@id=\"frslistcontent\"]/li[" + i + "]/a/div[1]/span[2]/text()").toString();
                }

                String replyNum = page.getHtml().xpath("//*[@id=\"frslistcontent\"]/li[" + i + "]/a/div[3]/div/span/text()").toString();
                // 有可能在下一行
                if (StringUtils.isBlank(replyNum)) {
                    replyNum = page.getHtml().xpath("//*[@id=\"frslistcontent\"]/li[" + i + "]/a/div[2]/div/span/text()").toString();
                }

                String userName = page.getHtml().xpath("//*[@id=\"frslistcontent\"]/li[" + i + "]/div/div[2]/div/span/text()").toString();
                //
                if (StringUtils.isBlank(title) || StringUtils.isBlank(userName)) {
                    continue;
                }
                
                //
                post.setType(type);
                post.setTitle(title);
                post.setReplyNum(Integer.parseInt(replyNum == null ? "0" : replyNum));
                post.setUserName(userName);
                postList.add(post);
            }
            // 添加到pipeline
            page.putField("postList", postList);
        } catch (Exception e) {
            log.error("crawlPost error {}", e);
        }
    }

    public static void main(String[] args) {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        // 设置动态转发代理，使用定制的ProxyProvider
        httpClientDownloader.setProxyProvider(CrowProxyProvider.from(new Proxy("forward.xdaili.cn", 80)));
        Spider.create(new PostProcessor())//
                .addUrl("http://tieba.baidu.com/f?kw=太原工业学院&ie=utf-8&pn=0")//
                .addPipeline(new PostDownloadPipeline())//
                .thread(1)//
                .run();
    }
}
