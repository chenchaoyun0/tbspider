package com.megvii.dzh.spider.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.vo.NameValue;
import tk.mybatis.mapper.common.Mapper;

public interface CommentMapper extends Mapper<Comment> {

    @Select("select user_name as name,count(1) as value from `comment` where time>='${year}-01-01 00:00:00' AND time<='${year}-12-31 23:59:59' GROUP BY user_name ORDER BY value desc LIMIT #{limit}")
    List<NameValue> getActiveUser(@Param("year") String year,@Param("limit") int limit);

    @Select("select user_name as name,count(1) as value from `comment` GROUP BY user_name ORDER BY value desc LIMIT #{limit}")
    List<NameValue> getActiveUserBar(@Param("limit")int limit);
}
