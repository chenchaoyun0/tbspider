package com.megvii.dzh.spider.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.utils.NumberUtils;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonthVo;
import com.megvii.dzh.spider.domain.vo.PostYears;
import com.megvii.dzh.spider.domain.vo.Record;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IUserTbsService;
import com.megvii.dzh.spider.service.IWordDivideService;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/")
@Controller
@Slf4j
public class AnalysisController {
    @Autowired
    private BootConfig bootConfig;
    @Autowired
    private IPostService postService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserTbsService userTbsService;
    @Autowired
    private IWordDivideService wordDivideService;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/postTitleWordCloud")
    public String getPostTitleWord() {
        return "postTitleWordCloud";
    }

    /**
     * 帖子标题的热点词汇
     * 
     * @param years
     * @return
     */
    @RequestMapping(value = "/postTitleWord")
    @ResponseBody
    public List<NameValue> postTitleWord(int year,int limit) {
        List<NameValue> nameValuesByYear = postService.nameValuesByYear(year+"", limit);
        return nameValuesByYear;
    }
    // ------------------------------***我是分割线***--------------------------------------------//
    @RequestMapping(value = "/activeUser")
    public String activeUser() {
        return "activeUser";
    }
    /**
     * 20大活跃用户，按年分组
     * 
     * @param years
     * @return
     */
    @RequestMapping(value = "/getActiveUser")
    @ResponseBody
    public List<NameValue> getActiveUser(String year,int limit) {
        List<NameValue> nameValuesByYear = commentService.getActiveUser(year,limit);
        return nameValuesByYear;
    }
    // ------------------------------***我是分割线***--------------------------------------------//
    @RequestMapping(value = "/activeUserBar")
    public String activeUserBar() {
        return "activeUserBar";
    }
    /**
     * 20大活跃用户，按年分组
     * 
     * @param years
     * @return
     */
    @RequestMapping(value = "/getActiveUserBar")
    @ResponseBody
    public List<NameValue> getActiveUserBar(int limit) {
        List<NameValue> nameValuesByYear = commentService.getActiveUserBar(limit);
        return nameValuesByYear;
    }
    // ------------------------------***我是分割线***--------------------------------------------//
    @RequestMapping(value = "/userFansBar")
    public String userFansBar() {
        return "userFansBar";
    }
    /**
     * 20大活跃用户，按年分组
     * 
     * @param years
     * @return
     */
    @RequestMapping(value = "/getUserFansBar")
    @ResponseBody
    public List<NameValue> getUserFansBar(int limit) {
        return userService.getUserFansBar(limit);
    }
    // ------------------------------***我是分割线***--------------------------------------------//

    /**
     * 发帖用户与不发帖比例
     * 
     * @return
     */
    @RequestMapping(value = "/postAndNo")
    public String postAndNo() {
        return "postAndNo";
    }

    @RequestMapping(value = "/getPostAndNo")
    @ResponseBody
    public List<NameValue> getPostAndNo() {
        List<NameValue> result = postService.getPostAndNo();
        return result;
    }

    // ------------------------------***我是分割线***--------------------------------------------//
    /**
     * 有回复帖子与没回复比例
     * 
     * @return
     */
    @RequestMapping(value = "/postHasRepAndNo")
    public String postHasRepAndNo() {
        return "postHasRepAndNo";
    }

    @RequestMapping(value = "/getPostHasRepAndNo")
    @ResponseBody
    public List<NameValue> getPostHasRepAndNo() {
        List<NameValue> result = postService.getPostHasRepAndNo();
        return result;
    }

    // ------------------------------***我是分割线***--------------------------------------------//
    /**
     * 测试曲线图
     * 
     * @return
     */
    @RequestMapping(value = "/demo")
    public String demo() {
        return "demo";
    }
    @RequestMapping(value = "/getDemo")
    @ResponseBody
    public List<Record> getDemo() {
        List<Record> records = new ArrayList<Record>();
        for (int i = 0; i < 100; i++) {
            Record record = new Record();
            record.setDate(new Timestamp(System.currentTimeMillis()));
            record.setHum(NumberUtils.randomInt(10)+"");
            record.setPa(NumberUtils.randomInt(10)+"");
            record.setRain(NumberUtils.randomInt(10)+"");
            record.setTaizhan_num("A0001");
            record.setTem(NumberUtils.randomInt(10)+"");
            record.setWin_dir(NumberUtils.randomInt(10)+"");
            record.setWin_sp(NumberUtils.randomInt(10)+"");
          //将时间转换成给定格式便于echarts的X轴日期坐标显示
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");             
            String str = sdf.format(record.getDate());
            record.setDateStr(str);
            records.add(record);
        }
        
        return records;
    }
    
    // ------------------------------***我是分割线***--------------------------------------------//
    /**
     * 年发帖量曲线图
     * 
     * @return
     */
    @RequestMapping(value = "/postYears")
    public String postYears() {
        return "postYears";
    }
    @RequestMapping(value = "/postMonth")
    public String postMonth() {
        return "postMonth";
    }
    @RequestMapping(value = "/postHours")
    public String postHours() {
        return "postHours";
    }
    @RequestMapping(value = "/getPostGroupBy")
    @ResponseBody
    public List<PostYears> getPostGroupBy(String groupBy) {
       return postService.getPostGroupBy(groupBy);
    }
    
    @RequestMapping(value = "/getPostGroupByMonth")
    @ResponseBody
    public List<PostGroupByMonthVo> getPostGroupByMonth() {
        return postService.getPostGroupByMonth();
    }
    
    // ------------------------------***我是分割线***--------------------------------------------//
    /**
     * 用户等级分布
     * 
     * @return
     */
    @RequestMapping(value = "/userLevel")
    public String userLevel() {
        return "userLevel";
    }

    @RequestMapping(value = "/getUserLevel")
    @ResponseBody
    public List<NameValue> getUserLevel() {
        List<NameValue> result = userTbsService.getUserLevel();
        return result;
    }
    // ------------------------------***我是分割线***--------------------------------------------//
    /**
     * 年度的十大热帖
     * 
     * @return
     */
    @RequestMapping(value = "/postTitlesyear")
    public String postTitlesyear() {
        return "postTitlesyear";
    }
    
    @RequestMapping(value = "/getPostTitlesyear")
    @ResponseBody
    public List<NameValue> getPostTitlesyear(String year) {
        List<NameValue> result = postService.getPostTitlesyear(year);
        return result;
    }
    // ------------------------------***我是分割线***--------------------------------------------//
    @RequestMapping(value = "/tbNameWordCloud")
    public String tbNameWordCloud() {
        return "tbNameWordCloud";
    }
    /**
     * 用户关注贴吧词云
     * 
     * @param years
     * @return
     */
    @RequestMapping(value = "/getTbNameWordCloud")
    @ResponseBody
    public List<NameValue> getTbNameWordCloud(int limit) {
        List<NameValue> nameValuesByYear = userTbsService.getTbNameWordCloud(limit);
        return nameValuesByYear;
    }

    /**
     * 男女比例
     * 
     * @return
     */
    @RequestMapping(value = "/userGender")
    public String userGender() {
        return "userGender";
    }

    @RequestMapping(value = "/getUserGender")
    @ResponseBody
    public List<NameValue> getUserGender() {
        return userService.getUserGender();
    }
}
