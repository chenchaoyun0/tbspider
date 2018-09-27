package com.megvii.spider.test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.lang3.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.spider.SpiderApplication;
import com.megvii.dzh.spider.enums.WordDivideType;
import com.megvii.dzh.spider.po.Comment;
import com.megvii.dzh.spider.po.NameValue;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.po.User;
import com.megvii.dzh.spider.po.WordDivide;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IUserTbsService;
import com.megvii.dzh.spider.service.IWordDivideService;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = {SpiderApplication.class})
@Slf4j
public class TestService {

    @Autowired
    private IPostService postService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserTbsService userTbsService;
    @Autowired
    private IWordDivideService wordDivideService;

    private ExecutorService saveExecutor = Executors.newFixedThreadPool(200,new DefaultThreadFactory("saveExecutor"));

    @Test
    public void test() {
        List<NameValue> nameValues = wordDivideService.nameValues(WordDivideType.POST_TITLE, 1000);
        log.info("---> size {} , data {}", nameValues.size(), JSONObject.toJSONString(nameValues));
    }

    @Test
    public void test2() {
        List<NameValue> nameValuesByYear = postService.nameValuesByYear("2016", 1000);
        log.info("---> size {} data {}",nameValuesByYear.size(),JSONObject.toJSONString(nameValuesByYear));
        
    }
    @Test
    public void test3() {
        List<NameValue> postAndNo = postService.getPostAndNo();
        log.info("---> size {} data {}",postAndNo.size(),JSONObject.toJSONString(postAndNo));
    }
    @Test
    public void test4() {
        List<NameValue> postAndNo = postService.getPostHasRepAndNo();
        log.info("---> size {} data {}",postAndNo.size(),JSONObject.toJSONString(postAndNo));
    }
}
