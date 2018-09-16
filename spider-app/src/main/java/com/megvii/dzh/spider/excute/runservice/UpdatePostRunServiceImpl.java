package com.megvii.dzh.spider.excute.runservice;

import java.util.Date;
import java.util.Map;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.spider.mapper.PostMapper;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.po.PostUser;
import com.megvii.dzh.spider.utils.DateConvertUtils;
import com.megvii.dzh.spider.utils.SpringUtils;
import com.megvii.dzh.spider.webmagic.processors.PostDetailProcessor;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;

@Slf4j
public class UpdatePostRunServiceImpl implements RunService {

    private Post post;

    private PostMapper postMapper = SpringUtils.getBean(PostMapper.class);

    public UpdatePostRunServiceImpl(Post post) {
        this.post = post;
    }

    @Override
    public void run() {

        try {
            String postUrl = post.getPostUrl();
            ResultItems resultItems = Spider.create(new PostDetailProcessor()).get(postUrl);
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                if (entry.getKey().equals("time")) {
                    String time = (String) entry.getValue();
                    Date date = DateConvertUtils.parse(time, DateConvertUtils.DATE_TIME_NO_SS);
                    post.setTime(date);
                }
                if (entry.getKey().equals("content")) {
                    String content = (String) entry.getValue();
                    post.setContent(content);
                }
                if (entry.getKey().equals("postUser")) {
                    PostUser postUser = (PostUser) entry.getValue();
                }
            }
            //
            int update = postMapper.updateByPrimaryKey(post);
            log.debug("---> update {}", update);
        } catch (Exception e) {
            log.error("UpdatePostRunServiceImpl run error {}", e);
        }


    }

}
