package com.megvii.dzh.spider.webmagic.pipelines;

import java.util.List;
import java.util.Map;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.megvii.dzh.spider.config.BootConfig;
import com.megvii.dzh.spider.excute.PostSaveExcute;
import com.megvii.dzh.spider.excute.WordDivideSaveExcute;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.po.WordDivide;
import com.megvii.dzh.spider.utils.SpringUtils;
import com.megvii.dzh.spider.webmagic.processors.PostDetailProcessor;
import com.megvii.dzh.spider.webmagic.processors.PostProcessor;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
@Slf4j
public class PostDownloadPipeline implements Pipeline {

    @Autowired
    private BootConfig bootConfig;
    
    @Override
    public void process(ResultItems resultItems, Task task) {
        //处理线程池
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().equals("postList")) {
                List<Post> postList = (List<Post>) entry.getValue();
                log.info("---------->当前页爬取的帖子 size {}",postList.size());
                try {
                    for (int i = 0; i < postList.size(); i++) {
                        Post post = postList.get(i);
                        //另外线程入库
                        bootConfig.getThreadPoolPost().putAnRun(post, PostSaveExcute.class);
                        //保存分词
                        String title = post.getTitle();
                        List<Word> list = WordSegmenter.seg(title);
                        //另外线程保存分词
                        for (Word word : list) {
                            WordDivide wordDivide = new WordDivide();
                            wordDivide.setWord(word.getText());
                            wordDivide.setType(2);
                            bootConfig.getThreadPoolWordDivide().putAnRun(wordDivide, WordDivideSaveExcute.class);
                        }
                    }
                } catch (Exception e) {
                    log.error("putAnRun error", e);
                }
            }
        }
    }

}
