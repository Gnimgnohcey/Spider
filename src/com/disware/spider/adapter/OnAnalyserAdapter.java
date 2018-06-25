package com.disware.spider.adapter;

import com.disver.spider.api.core.Context;
import com.disver.spider.api.core.SpiderBean;
import com.disware.spider.listener.OnAnalyserListener;

import java.util.List;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/24
 */
public class OnAnalyserAdapter<T extends SpiderBean> implements OnAnalyserListener<T> {
    @Override
    public List<String> preAnalyse(Context context, List<String> currentField) {
        return currentField;
    }

    @Override
    public SpiderBean process(Context context, T spiderBean, Map<String, List<String>> param, int index) {
        return spiderBean;
    }
}
