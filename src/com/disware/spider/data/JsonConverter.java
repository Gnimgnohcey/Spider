package com.disware.spider.data;

import com.alibaba.fastjson.JSON;
import com.disver.spider.api.core.SpiderBean;
import com.disver.spider.api.data.SpiderConverter;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 */
public class JsonConverter implements SpiderConverter<String> {
    @Override
    public String convert(SpiderBean spiderBean) {
        return JSON.toJSONString(spiderBean);
    }
}
