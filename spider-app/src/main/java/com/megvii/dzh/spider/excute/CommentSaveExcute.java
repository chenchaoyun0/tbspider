package com.megvii.dzh.spider.excute;

import java.util.concurrent.ArrayBlockingQueue;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.mapper.CommentMapper;
import com.megvii.dzh.spider.po.Comment;
import com.megvii.dzh.spider.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentSaveExcute extends ExpandThread<Comment> {

    private CommentMapper CommentMapper = SpringUtils.getBean(CommentMapper.class);

    public CommentSaveExcute(ArrayBlockingQueue<Comment> arrayBlockingQueue) {
        super(arrayBlockingQueue);
    }

    @Override
    public RunService perform(Comment Comment) {
        try {
            int insert = CommentMapper.insert(Comment);
            log.debug("insert(Comment) {} {}",JSONObject.toJSONString(Comment), insert);

        } catch (Exception e) {
            log.error("perform error Comment {}",JSONObject.toJSONString(Comment), e);
        } finally {
        }
        return null;
    }

    @Override
    public void writeBack(ResultBackObject resultBackObject) {}


}
