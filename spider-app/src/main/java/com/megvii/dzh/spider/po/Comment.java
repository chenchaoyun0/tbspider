package com.megvii.dzh.spider.po;

import java.util.Date;
import lombok.Data;

@Data
public class Comment {
    private Long id;

    private String userName;

    private String title;

    private Date time;

    private String userDevice;
    
    private String content;
}