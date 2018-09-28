package com.megvii.dzh.spider.service;

import java.util.List;
import com.megvii.dzh.spider.common.enums.WordDivideType;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.domain.vo.NameValue;

public interface IWordDivideService extends IBaseService<WordDivide> {
    /**
     * 热点词汇
     * 
     * @param years
     * @return
     */
    List<NameValue> nameValues(WordDivideType type,long limit);

}
