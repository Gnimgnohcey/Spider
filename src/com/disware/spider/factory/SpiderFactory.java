package com.disware.spider.factory;

import com.disver.spider.api.core.Context;
import com.disware.spider.core.Configuration;
import com.disware.spider.core.Spider;
import com.disware.spider.core.SpiderContext;
import com.disware.spider.data.FilePipeline;
import com.disware.spider.data.SpiderHeader;
import com.disware.spider.io.SpiderDownloader;

/**
 * @author 4everlynn
 * Create at 2018/6/22
 */
public class SpiderFactory {
    /**
     * 根据上下文创建出爬虫
     * 该方法用于子爬虫类分析
     *
     * @param context 上下文
     * @return 获得上下文能力的爬虫对象
     */
    public static Spider superSpider(Context context) {
        return new Spider(context);
    }

    /**
     * 为了节省用户配置的时间，提供一套约定俗成的配置
     * 这个默认的爬虫配置了官方的数据流管道FilePipeline
     *
     * @param target 目标网址
     * @param path   日志文件存放位置
     * @return 默认配置的Spider
     */
    public static Spider defaultSpider(String target, String path) {
        return new Spider(new SpiderContext()
                .target(target)
                .downloader(new SpiderDownloader())
                .header(new SpiderHeader().include("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36")
                        .include("Content-Type", "text/html"))
                .configuration(new Configuration()
                        .pipeline(new FilePipeline(path))
                        .interval(100))
                .connect())
                .pool(10);
    }
}
