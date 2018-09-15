package com.megvii.dzh.spider.po;

import lombok.Data;

@Data
public class User {
    private Long id;

    private String userName;

    private Integer gender;

    private Double tbAge;

    private Integer postCount;

    private Integer followCount;

    private Integer fansCount;

    private String userHeadUrl;

    private String userHomeUrl;
}