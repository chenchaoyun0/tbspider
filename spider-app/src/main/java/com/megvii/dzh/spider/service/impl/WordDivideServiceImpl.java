package com.megvii.dzh.spider.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.megvii.dzh.spider.enums.WordDivideType;
import com.megvii.dzh.spider.mapper.WordDivideMapper;
import com.megvii.dzh.spider.po.NameValue;
import com.megvii.dzh.spider.po.WordDivide;
import com.megvii.dzh.spider.service.IWordDivideService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WordDivideServiceImpl extends BaseServiceImpl<WordDivide> implements IWordDivideService {

    @Autowired
    private WordDivideMapper wordDivideMapper;

    @Override
    public List<NameValue> nameValues(WordDivideType type,long limit) {
        try {
           return wordDivideMapper.nameValues(type.getCode(),limit);
        } catch (Exception e) {
            log.error("postTitleWord error {}", e);
        }
        return null;
    }


}
