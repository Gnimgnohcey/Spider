package com.disware.spider.data;

import com.disver.spider.api.core.SpiderBean;
import com.disver.spider.api.data.SpiderData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 */
public class KittySpiderData extends HashMap<String, SpiderBean> implements SpiderData {
    @Override
    public Map<String, SpiderBean> data() {
        return this;
    }

    @Override
    public SpiderBean data(String token) {
        return get(token);
    }

    @Override
    public SpiderData reset(String token, SpiderBean spiderBean) {
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
}
