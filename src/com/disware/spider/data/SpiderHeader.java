package com.disware.spider.data;

import java.util.HashMap;

/**
 * @author 4everlynn
 * Create at 2018/6/8
 * 实现一个头类用于连续进行浏览器头的增加
 */
public class SpiderHeader extends HashMap<String, String> {
    public SpiderHeader include(String key, String value) {
        put(key, value);
        return this;
    }

    public SpiderHeader exclude(String key) {
        remove(key);
        return this;
    }

    public SpiderHeader data() {
        return this;
    }
}
