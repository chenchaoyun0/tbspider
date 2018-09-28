package com.megvii.dzh.spider.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.domain.po.UserTbs;
import com.megvii.dzh.spider.domain.vo.NameValue;
import tk.mybatis.mapper.common.Mapper;

public interface UserTbsMapper extends Mapper<UserTbs>{

    @Select("select tb_level as name,count(1) as value from user_tbs where tb_name='"+Constant.TIEBA_NAME+"' GROUP BY tb_level")
    List<NameValue> getUserLevel();

    @Select("select tb_name as name,count(1) as value from user_tbs where tb_name!='"+Constant.TIEBA_NAME+"' GROUP BY tb_name ORDER BY value desc LIMIT #{0}")
    List<NameValue> getTbNameWordCloud(int limit);
}