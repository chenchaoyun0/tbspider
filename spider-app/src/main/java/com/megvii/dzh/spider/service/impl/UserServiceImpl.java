 package com.megvii.dzh.spider.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.mapper.UserMapper;
import com.megvii.dzh.spider.service.IUserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    
    @Override
    public List<NameValue> getUserFansBar(int limit) {
        try {
            List<NameValue> list = userMapper.getUserFansBar(limit);
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getUserLevel error {}", e);
        }
        return null;
    }

    @Override
    public List<NameValue> getUserGender() {
        try {
            List<NameValue> list = userMapper.getUserGender();
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getUserGender error {}", e);
        }
        return null;
    }


}
