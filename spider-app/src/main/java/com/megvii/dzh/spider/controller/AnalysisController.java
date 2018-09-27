package com.megvii.dzh.spider.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.megvii.dzh.spider.config.BootConfig;
import com.megvii.dzh.spider.po.NameValue;
import com.megvii.dzh.spider.po.Record;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IUserTbsService;
import com.megvii.dzh.spider.service.IWordDivideService;
import com.megvii.dzh.spider.utils.NumberUtils;
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
    public List<NameValue> postTitleWord() {
        List<NameValue> nameValuesByYear = postService.nameValuesByYear("2018", 500);
        return nameValuesByYear;
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
     * 有回复帖子与没回复比例
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
     * 男女比例
     * 
     * @return
     */
    @RequestMapping(value = "/gender")
    public String getGenderEcharts() {
        return "gender";
    }

    @RequestMapping(value = "/getGender")
    @ResponseBody
    public List<NameValue> getGender() {
        List<NameValue> result = new ArrayList<>();
        return result;
    }
}
