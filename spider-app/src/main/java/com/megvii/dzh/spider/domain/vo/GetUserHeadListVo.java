package com.megvii.dzh.spider.domain.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class GetUserHeadListVo implements Serializable {

  private String userName;

  private String userHeadUrl;

  private Long replyNum;
}
