package com.disware.spider.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 4everlynn
 * Create at 2018/6/22
 */
public class FileManager {
    public static void saveText(String url, String target) {
        FileOutputStream stream = null;
        try {
            File file = new File(url.substring(0, url.lastIndexOf("/")));
            if (!file.exists()) {
                file.mkdirs();
            }
            stream = new FileOutputStream(url);
            stream.write(target.getBytes());
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert stream != null;
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
