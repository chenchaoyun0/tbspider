package com.megvii.dzh.spider.common.utils;

public class URLGeneratedUtil {

    public final static String PREFIX = "http://tieba.baidu.com";


    public static String generatePostURL(String url) {
        return PREFIX + url;
    }

    public static String generateHttpURL(String url) {
        return url;
    }
}
