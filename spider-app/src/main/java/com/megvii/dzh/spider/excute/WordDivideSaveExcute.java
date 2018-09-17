package com.megvii.dzh.spider.excute;

import java.util.concurrent.ArrayBlockingQueue;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.mapper.WordDivideMapper;
import com.megvii.dzh.spider.po.WordDivide;
import com.megvii.dzh.spider.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WordDivideSaveExcute extends ExpandThread<WordDivide> {

    private WordDivideMapper wordDivideMapper = SpringUtils.getBean(WordDivideMapper.class);

    public WordDivideSaveExcute(ArrayBlockingQueue<WordDivide> arrayBlockingQueue) {
        super(arrayBlockingQueue);
    }

    @Override
    public RunService perform(WordDivide wordDivide) {
        try {
            int insert = wordDivideMapper.insert(wordDivide);
            log.info("insert(post) {} {}",JSONObject.toJSONString(wordDivide), insert);
        } catch (Exception e) {
            log.error("perform wordDivide {}",JSONObject.toJSONString(wordDivide), e);
        } finally {
        }
        return null;
    }

    @Override
    public void writeBack(ResultBackObject resultBackObject) {}


}
