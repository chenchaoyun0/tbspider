package com.megvii.dzh.spider.po;

import java.util.Date;
import lombok.Data;

@Data
public class WordDivide {
    private Long id;

    private String word;

    private Integer type;
    
    private Date time;
}