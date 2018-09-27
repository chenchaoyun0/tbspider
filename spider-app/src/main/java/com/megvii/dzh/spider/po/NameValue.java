package com.megvii.dzh.spider.po;

import lombok.Data;

@Data
public class NameValue {

    private String name;

    private Integer value;

    public NameValue(String name, Integer value) {
        super();
        this.name = name;
        this.value = value;
    }

    public NameValue() {
        super();
        // TODO Auto-generated constructor stub
    }

}
