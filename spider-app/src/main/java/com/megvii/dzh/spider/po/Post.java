package com.megvii.dzh.spider.po;

import java.util.Date;
import lombok.Data;
@Data
public class Post {
    private Long id;

    private String userName;

    private String title;

    private Integer replyNum;

    private Date time;

    private String content;
}