package com.megvii.dzh.spider.service;

import java.util.List;
import com.megvii.dzh.spider.enums.WordDivideType;
import com.megvii.dzh.spider.po.NameValue;
import com.megvii.dzh.spider.po.WordDivide;

public interface IWordDivideService extends IBaseService<WordDivide> {
    /**
     * 热点词汇
     * 
     * @param years
     * @return
     */
    List<NameValue> nameValues(WordDivideType type,long limit);

}
