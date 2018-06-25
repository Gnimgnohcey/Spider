package com.disware.spider.data;

import com.alibaba.fastjson.JSON;
import com.disver.spider.api.core.Context;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/23
 */
public class SpiderRequest {
    public static Map<String, String> header = new SpiderHeader();

    private SpiderRequest() {
    }

    public static SpiderRequest fromContext(Context context) {
        header = context.customHeaders();
        return new SpiderRequest().headers(header);
    }

    private SpiderRequest headers(Map<String, String> header) {
        SpiderRequest.header = header;
        return this;
    }

    public String get(String target) {
        try {
            return Jsoup.connect(target)
                    .ignoreContentType(true)
                    .headers(header)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String post(String target) {
        return post(target, null);
    }

    public String post(String target, RequestData data) {
        try {
            return Jsoup.connect(target)
                    .ignoreContentType(true)
                    .data("spider", "4everlynn")
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Accept", "text/plain, */*; q=0.01")
                    .requestBody(JSON.toJSONString(data))
                    .post()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
