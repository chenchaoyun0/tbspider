package com.megvii.dzh.spider.common.utils;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class SpiderFileUtils {

  public static void writeString2local(String data, String targetName) {
    try {
      FileUtils.write(new File(targetName), data,"utf8");
    } catch (IOException e) {
      log.error("writeString2local error {}", e);
    }
  }
}
