package com.megvii.dzh.spider.excute;

import java.util.concurrent.ArrayBlockingQueue;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.mapper.UserTbsMapper;
import com.megvii.dzh.spider.po.UserTbs;
import com.megvii.dzh.spider.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserTbsSaveExcute extends ExpandThread<UserTbs> {

    private UserTbsMapper UserTbsMapper = SpringUtils.getBean(UserTbsMapper.class);

    public UserTbsSaveExcute(ArrayBlockingQueue<UserTbs> arrayBlockingQueue) {
        super(arrayBlockingQueue);
    }

    @Override
    public RunService perform(UserTbs userTbs) {
        try {
            int insert = UserTbsMapper.insert(userTbs);
            log.debug("insert(UserTbs) {} {}",JSONObject.toJSONString(userTbs), insert);

        } catch (Exception e) {
            log.error("perform error userTbs {}",JSONObject.toJSONString(userTbs), e);
        } finally {
        }
        return null;
    }

    @Override
    public void writeBack(ResultBackObject resultBackObject) {}


}
