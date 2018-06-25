package com.disware.spider.data;

import java.util.HashMap;

/**
 * @author 4everlynn
 * Create at 2018/6/8
 */
public class RequestData extends HashMap<String, String> {
    public RequestData include(String key, String value) {
        put(key, value);
        return this;
    }

    public RequestData exclude(String key) {
        remove(key);
        return this;
    }
}
