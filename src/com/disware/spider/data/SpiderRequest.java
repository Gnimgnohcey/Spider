package com.disware.spider.data;

import com.alibaba.fastjson.JSON;
import com.disver.spider.api.core.Context;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/23
 * 接口请求类
 */
public class SpiderRequest {
    public static Map<String, String> header = new SpiderHeader();

    private SpiderRequest() {
    }

    /**
     * 从上下文中创建出请求类
     *
     * @param context 上下文
     * @return 新的请求类
     */
    public static SpiderRequest fromContext(Context context) {
        header = context.customHeaders();
        return new SpiderRequest().headers(header);
    }

    /**
     * 请求时所需要带的头
     *
     * @param header 请求头Map
     * @return 当前类
     */
    private SpiderRequest headers(Map<String, String> header) {
        SpiderRequest.header = header;
        return this;
    }

    /**
     * 发起GET请求
     *
     * @param target 目标接口
     * @return 结果
     */
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

    /**
     * 发起不带参数的POST请求
     *
     * @param target 目标接口
     * @return 结果
     */
    public String post(String target) {
        return post(target, null);
    }

    /**
     * 发起带RequestBody的POST请求
     *
     * @param target 目标接口
     * @param data   RequestBody
     * @return 结果
     */
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
