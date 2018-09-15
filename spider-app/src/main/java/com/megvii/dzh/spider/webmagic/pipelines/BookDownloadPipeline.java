package com.megvii.dzh.spider.webmagic.pipelines;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.concurrent.pool.ThreadPool;
import com.megvii.dzh.spider.performs.BookDownloadThread;
import com.megvii.dzh.spider.po.BookPo;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
@Slf4j
public class BookDownloadPipeline implements Pipeline {

    @Autowired
    private ThreadPool threadPool;

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().equals("listBookPo")) {
                List<BookPo> listBookPo = (List<BookPo>) entry.getValue();
                log.info("----------> " + JSONObject.toJSONString(listBookPo));
                try {
                    for (int i = 0; i < listBookPo.size(); i++) {
                        threadPool.putAnRun(listBookPo.get(i), BookDownloadThread.class);
                    }
                } catch (Exception e) {
                    log.error("putAnRun error", e);
                }
            }
        }
    }

}
