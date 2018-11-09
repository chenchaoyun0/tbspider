package com.megvii.dzh.spider.mapper;

import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.vo.GetUserHeadListVo;
import com.megvii.dzh.spider.domain.vo.NameValue;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface CommentMapper extends Mapper<Comment> {

  @Select("select user_name as name,count(1) as value from `comment` where time>='${yearBegin}-01-01 00:00:00' AND time<='${yearEnd}-12-31 23:59:59' GROUP BY user_name ORDER BY value desc LIMIT #{limit}")
  List<NameValue> getActiveUser(@Param("yearBegin") int yearBegin,@Param("yearEnd") int yearEnd, @Param("limit") int limit);

  @Select("select user_name as name,count(1) as value from `comment` GROUP BY user_name ORDER BY value desc LIMIT #{limit}")
  List<NameValue> getActiveUserBar(@Param("limit") int limit);

  @Select("select if(user_device='','未知',user_device) as name,count(1) as value from `comment` where user_device!='1' GROUP BY user_device ORDER BY value desc LIMIT #{arg0}")
  List<NameValue> getUserDevicePie(int limit);

  @Select("SELECT u.user_name as userName,u.user_head_url as userHeadUrl,count(1) as replyNum from `user` u LEFT JOIN `comment` c ON u.user_name=c.user_name GROUP BY u.user_name,u.user_head_url ORDER BY replyNum DESC LIMIT #{arg0}")
  List<GetUserHeadListVo> getUserHeadList(int limit);

}
