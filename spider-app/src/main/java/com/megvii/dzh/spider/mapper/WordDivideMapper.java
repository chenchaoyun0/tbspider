package com.megvii.dzh.spider.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.domain.vo.NameValue;
import tk.mybatis.mapper.common.Mapper;

public interface WordDivideMapper  extends Mapper<WordDivide>{

    List<NameValue> nameValues(@Param("type") Integer type,@Param("limit")long limit);
}