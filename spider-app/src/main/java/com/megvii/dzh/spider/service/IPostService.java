 package com.megvii.dzh.spider.service;

import java.util.List;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonth;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonthVo;
import com.megvii.dzh.spider.domain.vo.PostYears;

public interface IPostService extends IBaseService<Post> {
    /**
     * 热点词汇 按年
     * 
     * @param years
     * @return
     */
    List<NameValue> nameValuesByYear(String year,long limit);
    /**
     * 发帖用户与不发帖用户比
     * @return
     */
    List<NameValue> getPostAndNo();
    
    List<NameValue> getPostHasRepAndNo();
    
    List<PostYears> getPostGroupBy(String groupBy);
    
    List<PostGroupByMonthVo> getPostGroupByMonth();
    
    List<NameValue> getPostTitlesyear(String year);
    
}
