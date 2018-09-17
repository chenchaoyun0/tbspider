package com.megvii.dzh.spider.excute;

import java.util.concurrent.ArrayBlockingQueue;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.mapper.UserMapper;
import com.megvii.dzh.spider.po.User;
import com.megvii.dzh.spider.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserSaveExcute extends ExpandThread<User> {

    private UserMapper UserMapper = SpringUtils.getBean(UserMapper.class);

    public UserSaveExcute(ArrayBlockingQueue<User> arrayBlockingQueue) {
        super(arrayBlockingQueue);
    }

    @Override
    public RunService perform(User user) {
        try {
            int insert = UserMapper.insert(user);
            log.debug("insert(User) {} {}",JSONObject.toJSONString(user),insert);

        } catch (Exception e) {
            log.error("perform error User {}",JSONObject.toJSONString(user), e);
        } finally {
        }
        return null;
    }

    @Override
    public void writeBack(ResultBackObject resultBackObject) {}


}
