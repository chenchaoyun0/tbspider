package com.megvii.dzh.spider.webmagic.pipelines;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.megvii.dzh.spider.config.BootConfig;
import com.megvii.dzh.spider.excute.PostUrlSaveExcute;
import com.megvii.dzh.spider.po.PostUrl;
import com.megvii.dzh.spider.utils.URLGeneratedUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
@Slf4j
public class PostUrlSavePipeline implements Pipeline {

    @Autowired
    private BootConfig bootConfig;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //处理线程池
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().equals("listPosts")) {
                List<String> listPosts = (List<String>) entry.getValue();
                log.info("---------->保存帖子 size {}",listPosts.size());
                try {
                    for (int i = 0; i < listPosts.size(); i++) {
                        String url = listPosts.get(i);
                        PostUrl postUrl = new PostUrl();
                        postUrl.setPostUrl(URLGeneratedUtil.generatePostURL(url));
                        bootConfig.getThreadPostUrlDivide().putAnRun(postUrl, PostUrlSaveExcute.class);
                    }
                } catch (Exception e) {
                    log.error("putAnRun error", e);
                }
            }
        }
    }

}
