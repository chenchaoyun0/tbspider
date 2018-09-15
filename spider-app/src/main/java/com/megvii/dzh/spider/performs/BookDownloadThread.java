package com.megvii.dzh.spider.performs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.client.RestTemplate;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.po.BookPo;
import com.megvii.dzh.spider.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookDownloadThread extends ExpandThread<BookPo> {

    private RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);

    public BookDownloadThread(ArrayBlockingQueue<BookPo> arrayBlockingQueue) {
        super(arrayBlockingQueue);
    }

    @Override
    public RunService perform(BookPo bookPo) {
        String bookName = bookPo.getBookName();
        String photoUrl = bookPo.getPhotoUrl();
        InputStream fis = null;
        try {
            URL url = new URL(photoUrl);
            fis = url.openConnection().getInputStream();
            //
            String path="E:\\book\\";
            FileUtils.forceMkdir(new File(path));
            IOUtils.copy(fis, new FileOutputStream(path+ UUID.randomUUID().toString() + "-" + bookName + ".jpg"));
        } catch (Exception e) {
            log.error("openConnection error photoUrl {}", photoUrl, e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return null;
    }

    @Override
    public void writeBack(ResultBackObject resultBackObject) {}


}
