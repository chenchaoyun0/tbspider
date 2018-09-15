package com.megvii.dzh.spider.excute;

import java.util.concurrent.ArrayBlockingQueue;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.mapper.PostMapper;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostSaveExcute extends ExpandThread<Post> {

    private PostMapper postMapper = SpringUtils.getBean(PostMapper.class);

    public PostSaveExcute(ArrayBlockingQueue<Post> arrayBlockingQueue) {
        super(arrayBlockingQueue);
    }

    @Override
    public RunService perform(Post post) {
        try {
            int insert = postMapper.insert(post);
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
