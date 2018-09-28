 package com.megvii.dzh.spider.service;

import java.util.List;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.vo.NameValue;

public interface ICommentService extends IBaseService<Comment> {

    List<NameValue> getActiveUser(String year,int limit);

    List<NameValue> getActiveUserBar(int limit);

}
