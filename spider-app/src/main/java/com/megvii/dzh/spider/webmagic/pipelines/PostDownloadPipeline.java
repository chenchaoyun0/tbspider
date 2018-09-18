package com.megvii.dzh.spider.webmagic.pipelines;

import java.util.List;
import java.util.Map;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.megvii.dzh.spider.config.BootConfig;
import com.megvii.dzh.spider.excute.CommentSaveExcute;
import com.megvii.dzh.spider.excute.PostSaveExcute;
import com.megvii.dzh.spider.excute.UserSaveExcute;
import com.megvii.dzh.spider.excute.UserTbsSaveExcute;
import com.megvii.dzh.spider.excute.WordDivideSaveExcute;
import com.megvii.dzh.spider.po.Comment;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.po.User;
import com.megvii.dzh.spider.po.UserTbs;
import com.megvii.dzh.spider.po.WordDivide;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
@Slf4j
public class PostDownloadPipeline implements Pipeline {

    @Autowired
    private BootConfig bootConfig;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String url = resultItems.getRequest().getUrl();
        // 处理线程池
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().equals("post")) {
                Post post = (Post) entry.getValue();
                try {
                    // 另外线程入库
                    bootConfig.getThreadPoolPost().putAnRun(post, PostSaveExcute.class);
                    // 保存分词
                    String title = post.getTitle();
                    List<Word> list = WordSegmenter.seg(title);
                    // 另外线程保存分词
                    for (Word word : list) {
                        String text = word.getText();
                        if (text.matches("d++")) {
                            continue;
                        }
                        WordDivide wordDivide = new WordDivide();
                        wordDivide.setWord(text);
                        wordDivide.setType(2);
                        bootConfig.getThreadPoolWordDivide().putAnRun(wordDivide, WordDivideSaveExcute.class);
                    }
                } catch (Exception e) {
                    log.error("putAnRun error {}", url, e);
                }
            } else if (entry.getKey().equals("listComment")) {
                List<Comment> listComment = (List<Comment>) entry.getValue();
                try {

                    for (Comment comment : listComment) {
                        // 另外线程入库
                        bootConfig.getThreadCommentDivide().putAnRun(comment, CommentSaveExcute.class);
                        // 保存分词
                        String content = comment.getContent();
                        List<Word> list = WordSegmenter.seg(content);
                        // 另外线程保存分词
                        for (Word word : list) {
                            if (word.getText().matches("d++")) {
                                continue;
                            }
                            WordDivide wordDivide = new WordDivide();
                            wordDivide.setWord(word.getText());
                            wordDivide.setType(3);
                            bootConfig.getThreadPoolWordDivide().putAnRun(wordDivide, WordDivideSaveExcute.class);
                        }
                    }
                } catch (Exception e) {
                    log.error("putAnRun error {}", url, e);
                }
            } else if (entry.getKey().equals("userTbsList")) {
                List<UserTbs> userTbsList = (List<UserTbs>) entry.getValue();
                try {

                    for (UserTbs userTb : userTbsList) {
                        // 另外线程入库
                        bootConfig.getThreadUserTbsDivide().putAnRun(userTb, UserTbsSaveExcute.class);
                    }
                } catch (Exception e) {
                    log.error("putAnRun error {}", url, e);
                }
            } else if (entry.getKey().equals("user")) {
                User user = (User) entry.getValue();
                try {
                    // 另外线程入库
                    bootConfig.getThreadUserDivide().putAnRun(user, UserSaveExcute.class);
                    // 保存分词
                    String title = user.getUserName();
                    List<Word> list = WordSegmenter.seg(title);
                    // 另外线程保存分词
                    for (Word word : list) {
                        String text = word.getText();
                        if (text.matches("d++")) {
                            continue;
                        }
                        WordDivide wordDivide = new WordDivide();
                        wordDivide.setWord(text);
                        wordDivide.setType(1);
                        bootConfig.getThreadPoolWordDivide().putAnRun(wordDivide, WordDivideSaveExcute.class);
                    }
                } catch (Exception e) {
                    log.error("putAnRun error {}", url, e);
                }
            }
        }
    }

}
