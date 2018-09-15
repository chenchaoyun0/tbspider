package com.megvii.dzh.perfrom.bean;

import lombok.Data;

/**
 * @ClassName ResultBackObject
 * @Description 线程执行状态回写类
 * @Author shuliyao
 * @CreateTime 2018/7/23 下午4:02
 */
@Data
public class ResultBackObject {
  int code;
  boolean isSucceed;
  String describe;
}
