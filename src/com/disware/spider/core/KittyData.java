package com.disware.spider.core;

import com.disver.spider.api.core.Context;
import com.disver.spider.api.core.SpiderBean;
import com.disver.spider.api.core.SpiderConfiguration;
import com.disver.spider.api.core.SpiderEngine;
import com.disver.spider.api.data.SpiderConverter;
import com.disver.spider.api.data.SpiderData;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 */
public class KittyData implements SpiderEngine {
    private SpiderConverter converter;
    private SpiderConfiguration configuration;
    private boolean integrity;

    @Override
    public SpiderData analyse(SpiderBean... spiderBeans) {
        return null;
    }

    @Override
    public SpiderEngine converter(SpiderConverter spiderConverter) {
        return null;
    }

    @Override
    public SpiderEngine integrity(boolean integrity) {
        this.integrity = integrity;
        return this;
    }

    @Override
    public SpiderEngine configuration(SpiderConfiguration spiderConfiguration) {
        this.configuration = spiderConfiguration;
        return this;
    }

    @Override
    public void onCreate(Context context) {

    }

    @Override
    public void onDestroy() {

    }
}
