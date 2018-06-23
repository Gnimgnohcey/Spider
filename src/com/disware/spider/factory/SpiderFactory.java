package com.disware.spider.factory;

import com.disver.spider.api.core.Context;
import com.disware.spider.core.Configuration;
import com.disware.spider.core.Spider;
import com.disware.spider.core.SpiderContext;
import com.disware.spider.data.FilePipeline;
import com.disware.spider.data.SpiderHeader;

/**
 * @author 4everlynn
 * Create at 2018/6/22
 */
public class SpiderFactory {
    public static Spider superSpider(Context context) {
        return new Spider(context);
    }

    public static Spider defaultSpider(String target, String path) {
        return new Spider(new SpiderContext()
                .target(target)
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
