 package com.megvii.dzh.spider.service;

import java.util.List;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;

public interface IUserService extends IBaseService<User> {

    List<NameValue> getUserFansBar(int limit);

    List<NameValue> getUserGender();

}
