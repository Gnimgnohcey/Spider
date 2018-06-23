package com.disware.spider.data;

import com.disver.spider.api.core.SpiderEngine;
import com.disver.spider.api.data.SpiderData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 */
public class SpiderDataResult extends HashMap<String, Object> implements SpiderData {
    private SpiderEngine spiderEngine;

    public SpiderDataResult(SpiderEngine engine) {
        this.spiderEngine = engine;
    }

    @Override
    public Map<String, Object> data() {
        return this;
    }

    @Override
    public Object data(String token) {
        return get(token);
    }

    @Override
    public SpiderData reset(String token, Object spiderBean) {
        put(token, spiderBean);
        return this;
    }

    @Override
    public SpiderData remove(String token) {
        super.remove(token);
        return this;
    }

    @Override
    public Integer count() {
        return size();
    }

    @Override
    public SpiderEngine rollback() {
        return spiderEngine;
    }
}
