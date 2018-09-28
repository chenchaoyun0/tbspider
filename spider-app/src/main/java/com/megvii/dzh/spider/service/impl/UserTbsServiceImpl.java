 package com.megvii.dzh.spider.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.megvii.dzh.spider.domain.po.UserTbs;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostYears;
import com.megvii.dzh.spider.mapper.UserTbsMapper;
import com.megvii.dzh.spider.service.IUserTbsService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserTbsServiceImpl extends BaseServiceImpl<UserTbs> implements IUserTbsService {

    @Autowired
    private UserTbsMapper userTbsMapper;
    
    @Override
    public List<NameValue> getUserLevel() {
        try {
            List<NameValue> list = userTbsMapper.getUserLevel();
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getUserLevel error {}", e);
        }
        return null;
    }


}
