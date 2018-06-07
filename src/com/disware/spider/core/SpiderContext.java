package com.disware.spider.core;

import com.disver.spider.api.core.Context;
import com.disver.spider.api.core.SpiderConfiguration;

import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 */
public class SpiderContext implements Context{
    @Override
    public Object getDocument() {
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public Map<String, String> customHeaders() {
        return null;
    }

    @Override
    public Context configuration(SpiderConfiguration spiderConfiguration) {
        return null;
    }
}
