package com.megvii.dzh.spider.utils;

/**
 * Created by CrowHawk on 17/10/7.
 */
public class URLGeneratedUtil {

    public final static String PREFIX = "http://www.shopbop.ink/bookmanager";

    public static String generatePostURL(String url) {
        return PREFIX + url;
    }
}
