 package com.megvii.spider.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

public class UrlTest {
     public static void main(String[] args) throws UnsupportedEncodingException {
         //%E5%A4%AA%E5%8E%9F%E5%B7%A5%E4%B8%9A%E5%AD%A6%E9%99%A2&
         String encode = URLEncoder.encode("太原工业学院","UTF-8");
         System.out.println(encode);
         List<Word> words = WordSegmenter.seg("今天晚上还睡觉不");
         List<Word> wordss = WordSegmenter.segWithStopWords("今天晚上还睡觉不");
         System.out.println(words);
         System.out.println(wordss);
    }
}
