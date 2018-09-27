package com.megvii.dzh.spider.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import com.megvii.dzh.spider.po.CountGroupByUser;
import com.megvii.dzh.spider.po.Post;
import tk.mybatis.mapper.common.Mapper;

public interface PostMapper extends Mapper<Post>{
    @Select("select user_name as userName,count(1) postCount from post group by user_name order by postCount desc")
    List<CountGroupByUser> selectCountGroupByUser();
}