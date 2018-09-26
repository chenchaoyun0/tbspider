package com.megvii.dzh.spider.webmagic.pipelines;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.megvii.dzh.spider.po.Comment;
import com.megvii.dzh.spider.po.Post;
import com.megvii.dzh.spider.po.User;
import com.megvii.dzh.spider.po.UserTbs;
import com.megvii.dzh.spider.po.WordDivide;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IUserTbsService;
import com.megvii.dzh.spider.service.IWordDivideService;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
@Slf4j
public class PostDownloadPipeline implements Pipeline {

    
    @Autowired
    private IPostService postService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserTbsService userTbsService;
    @Autowired
    private IWordDivideService wordDivideService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String url = resultItems.getRequest().getUrl();
        // 处理线程池
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().equals("post")) {
                Post post = (Post) entry.getValue();
                try {
                    // 另外线程入库
                    postService.insert(post);
                    // 保存分词
                    String title = post.getTitle();
                    if(StringUtils.isBlank(title)){
                        continue;
                    }
                    List<Word> list = WordSegmenter.seg(title);
                    // 另外线程保存分词
                    for (Word word : list) {
                        String text = word.getText();
                        if (text.trim().matches("\\d++")) {
                            continue;
                        }
                        WordDivide wordDivide = new WordDivide();
                        wordDivide.setWord(text);
                        wordDivide.setType(2);
                        //
                        wordDivideService.insert(wordDivide);
                    }
                } catch (Exception e) {
                    log.error("putAnRun error {}", url, e);
                }
            } else if (entry.getKey().equals("listComment")) {
                List<Comment> listComment = (List<Comment>) entry.getValue();
                try {

                    for (Comment comment : listComment) {
                        // 另外线程入库
                        commentService.insert(comment);
                        // 保存分词
                        String content = comment.getContent();
                        if(StringUtils.isBlank(content)){
                            continue;
                        }
                        List<Word> list = WordSegmenter.seg(content);
                        // 另外线程保存分词
                        for (Word word : list) {
                            if (word.getText().trim().matches("\\d++")) {
                                continue;
                            }
                            WordDivide wordDivide = new WordDivide();
                            wordDivide.setWord(word.getText());
                            wordDivide.setType(3);
                            wordDivideService.insert(wordDivide);
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
                        userTbsService.insert(userTb);
                    }
                } catch (Exception e) {
                    log.error("putAnRun error {}", url, e);
                }
            } else if (entry.getKey().equals("user")) {
                User user = (User) entry.getValue();
                try {
                    // 另外线程入库
                    userService.insert(user);
                    // 保存分词
                    String title = user.getUserName();
                    List<Word> list = WordSegmenter.seg(title);
                    // 另外线程保存分词
                    for (Word word : list) {
                        String text = word.getText();
                        if (text.trim().matches("\\d++")) {
                            continue;
                        }
                        WordDivide wordDivide = new WordDivide();
                        wordDivide.setWord(text);
                        wordDivide.setType(1);
                        //
                        wordDivideService.insert(wordDivide);
                    }
                } catch (Exception e) {
                    log.error("putAnRun error {}", url, e);
                }
            }
        }
    }

}
