package com.megvii.dzh.spider.po;

import lombok.Data;

@Data
public class UserTbs {
    private Long id;

    private String userName;

    private String tbName;

    private Integer tbLevel;
}