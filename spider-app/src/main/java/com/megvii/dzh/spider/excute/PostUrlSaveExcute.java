package com.megvii.dzh.spider.excute;

import java.util.concurrent.ArrayBlockingQueue;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.mapper.PostUrlMapper;
import com.megvii.dzh.spider.po.PostUrl;
import com.megvii.dzh.spider.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostUrlSaveExcute extends ExpandThread<PostUrl> {

    private PostUrlMapper postUrlMapper = SpringUtils.getBean(PostUrlMapper.class);

    public PostUrlSaveExcute(ArrayBlockingQueue<PostUrl> arrayBlockingQueue) {
        super(arrayBlockingQueue);
    }

    @Override
    public RunService perform(PostUrl postUrl) {
        try {
            int insert = postUrlMapper.insert(postUrl);
            log.debug("insert(post) {}", insert);
        } catch (Exception e) {
            log.error("perform {}", e);
        } finally {
        }
        return null;
    }

    @Override
    public void writeBack(ResultBackObject resultBackObject) {}


}
