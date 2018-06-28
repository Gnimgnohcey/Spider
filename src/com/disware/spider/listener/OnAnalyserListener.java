package com.disware.spider.listener;

import com.disver.spider.api.core.Context;
import com.disver.spider.api.core.SpiderBean;

import java.util.List;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/22
 * 核心接口用于处理分析某个字段并进行后续处理，可代替Pipline
 * 更准确的说它就像是每个字段的Pipline
 */
public interface OnAnalyserListener<Target extends SpiderBean> {
    /**
     * 字段开始分析前调用该接口
     *
     * @param context      当前上下文
     * @param currentField 当前字段
     * @return 处理后的字段
     */
    List<String> preAnalyse(Context context, List<String> currentField);

    /**
     * 分析过程中调用的接口
     * 该接口在分析前后会被调用两次
     * 第一次是SpiderBean未被注入时
     * 第二次是注入完成后
     * 用于处理文件名、下载
     *
     * @param context    当前上下文
     * @param spiderBean 当前具体的spiderBean
     * @param param      该spiderBean所有的参数Map
     * @param index      当前运行下标
     * @return 注入完成后的爬虫类
     */
    SpiderBean process(Context context, Target spiderBean, Map<String, List<String>> param, int index);
}
