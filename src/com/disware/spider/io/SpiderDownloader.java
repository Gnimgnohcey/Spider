package com.disware.spider.io;

import com.disver.spider.api.core.SpiderFilter;
import com.disver.spider.api.io.Downloader;
import com.disware.spider.data.SpiderHeader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/9
 * 文件下载器，用户可从上下文中取得
 */
public class SpiderDownloader implements Downloader {
    private List<SpiderFilter> filters = new ArrayList<>();
    private SpiderHeader headers = new SpiderHeader();
    private String target;
    private String path;
    private String token;
    private Integer interval = 0;


    /**
     * 放置路径
     *
     * @param path 路径
     * @return 当前类
     */
    @Override
    public Downloader to(String path) {
        this.path = path;
        return this;
    }


    /**
     * 输出文件名
     *
     * @param token 文件名
     * @return 当前类
     */
    @Override
    public Downloader token(String token) {
        this.token = token;
        return this;
    }

    /**
     * @param target 目标文件
     * @return 当前类
     */
    @Override
    public Downloader put(String target) {
        this.target = target;
        return this;
    }

    /**
     * 开始下载
     */
    @Override
    public void start() {
        pause(interval);
        // 构造URL
        URL url = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            url = new URL(target);
            // 打开连接
            URLConnection connection = url.openConnection();
            // 设置请求头
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
            //设置请求超时为5s
            connection.setConnectTimeout(5 * 1000);
            // 输入流
            inputStream = connection.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf = new File(path);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            outputStream = new FileOutputStream(sf.getPath() + File.separator + token);
            while ((len = inputStream.read(bs)) != -1) {
                outputStream.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert outputStream != null;
                assert inputStream != null;
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Downloader filter(SpiderFilter... spiderFilters) {
        filters.addAll(Arrays.asList(spiderFilters));
        return this;
    }

    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载时间间隔
     *
     * @param integer 时间间隔
     * @return 当前类
     */
    @Override
    public Downloader interval(Integer integer) {
        this.interval = integer;
        return this;
    }

    /**
     * 设置下载时所带的浏览器头
     *
     * @param map 浏览器头Map
     * @return 当前类
     */
    @Override
    public Downloader headers(Map<String, String> map) {
        for (String key : map.keySet()) {
            headers.include(key, map.get(key));
        }
        return this;
    }

    @Override
    public Downloader header(String key, String value) {
        headers.include(key, value);
        return this;
    }

    @Override
    public Downloader excludeHeader(String key) {
        headers.exclude(key);
        return this;
    }

}
