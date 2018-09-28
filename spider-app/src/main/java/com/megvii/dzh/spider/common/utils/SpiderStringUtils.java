package com.megvii.dzh.spider.common.utils;

import java.net.URLEncoder;
import com.megvii.dzh.spider.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpiderStringUtils {
    /**
     * 将文本编码
     * 
     * @return
     */
    public static String getTieBaEncodeName(String str) {
        try {
            return URLEncoder.encode(str, Constant.UTF8);
        } catch (Exception e) {
            log.error("getTieBaEncodeName error {}", e);
        }
        return "";
    }
}
