 package com.megvii.dzh.spider.service.impl;

import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.vo.GetUserHeadListVo;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.mapper.CommentMapper;
import com.megvii.dzh.spider.service.ICommentService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<NameValue> getUserDevicePie(int limit) {
        try {
            List<NameValue> list = commentMapper.getUserDevicePie(limit);
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getUserDevicePie error {}", e);
        }
        return null;
    }

    @Override
    public List<NameValue> getActiveUser(int year,int limit) {
        try {
            int yearBegin=year;
            int yearEnd=year;
            if(year==0){
                yearBegin=2007;
                yearEnd=2018;
            }
            List<NameValue> list = commentMapper.getActiveUser(yearBegin,yearEnd,limit);
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getActiveUser error {}", e);
        }
        return null;
    }

    @Override
    public List<NameValue> getActiveUserBar(int limit) {
        try {
            List<NameValue> list = commentMapper.getActiveUserBar(limit);
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getActiveUserBar error {}", e);
        }
        return null;
    }


    @Override
    public List<GetUserHeadListVo> getUserHeadList(int limit) {
        try {
            List<GetUserHeadListVo> list= commentMapper.getUserHeadList(limit);
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getUserHeadList error {}", e);
        }
        return null;
    }
}
