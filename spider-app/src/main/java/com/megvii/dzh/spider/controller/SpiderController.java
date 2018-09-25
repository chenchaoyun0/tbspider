package com.megvii.dzh.spider.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.spider.config.BootConfig;
import com.megvii.dzh.spider.config.Constant;
import com.megvii.dzh.spider.utils.SpringUtils;
import com.megvii.dzh.spider.webmagic.pipelines.PostDownloadPipeline;
import com.megvii.dzh.spider.webmagic.processors.PostProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Spider;

@RestController
@RequestMapping("/")
@Api(description = "爬虫接口控制器")
@Slf4j
public class SpiderController {

    @Autowired
    private BootConfig bootConfig;

    private Spider spider = Spider.create(new PostProcessor());

    @ApiOperation("启动爬虫")
    @ApiImplicitParams({})
    @RequestMapping(value = "/startSpider", method = {RequestMethod.GET})
    public String startSpider() {
        try {

            if (spider.getThreadAlive()>0) {
                return "爬虫程序已启动,请勿重新请求";
            }
            // 启动多少个线程
            int spiderThreads = bootConfig.getSpiderThreads();
            // 贴吧名称
            String tbName = URLEncoder.encode(Constant.TIEBA_NAME, "UTF-8");
            // 开启爬虫
            spider.addUrl("http://tieba.baidu.com/f?kw=" + tbName + "&ie=utf-8&pn=0")//
                    .addPipeline(SpringUtils.getBean(PostDownloadPipeline.class))//
                    .thread(spiderThreads)//
                    .runAsync();
            //
            return "启动爬虫成功";
        } catch (Exception e) {
            log.error("startSpider error:{}", e);
        }
        return "启动失败";
    }

    @ApiOperation("爬虫状态")
    @ApiImplicitParams({})
    @RequestMapping(value = "/spiderStatus", method = {RequestMethod.GET})
    public String spiderStatus() {

        int threadAlive = spider.getThreadAlive();
        long pageCount = spider.getPageCount();
        //
        Map<String, Object> map = new HashMap<>();
        map.put("threadAlive", threadAlive);
        map.put("pageCount", pageCount);

        return JSONObject.toJSONString(map);
    }


}
