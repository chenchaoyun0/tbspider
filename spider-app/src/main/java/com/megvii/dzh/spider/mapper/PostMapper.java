package com.megvii.dzh.spider.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.vo.CountGroupByUser;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonth;
import com.megvii.dzh.spider.domain.vo.PostYears;
import tk.mybatis.mapper.common.Mapper;

public interface PostMapper extends Mapper<Post>{
    @Select("select user_name as userName,count(1) postCount from post group by user_name order by postCount desc")
    List<CountGroupByUser> selectCountGroupByUser();

    @Select("SELECT ${groupBy} as data,count(1) as count from post GROUP BY ${groupBy} ORDER BY ${groupBy}")
    List<PostYears> getPostGroupBy(@Param("groupBy") String groupBy);

    @Select("SELECT `year` as `year`,`month` as `month`,count(1) as count from post where `year`=#{year} and `month`=#{month}")
    PostGroupByMonth getPostGroupByMonth(@Param("year")int year,@Param("month")int month);

    @Select("SELECT title as name , reply_num as value from post where `year`=#{year} ORDER BY reply_num desc LIMIT 20")
    List<NameValue> getPostTitlesyear(String year);
}