package com.disware.spider.listener;

import com.disver.spider.api.core.Context;
import com.disver.spider.api.core.SpiderBean;

import java.util.List;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/22
 */
public interface OnAnalyserListener<Target extends SpiderBean> {
    List<String> preAnalyse(List<String> currentField);
    SpiderBean process(Context context, Target spiderBean, Map<String, List<String>> param, int index);
}
