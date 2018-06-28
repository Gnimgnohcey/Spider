package com.disware.spider.core;

import com.disver.spider.api.core.SpiderConfiguration;
import com.disver.spider.api.core.SpiderFilter;
import com.disver.spider.api.data.SpiderConverter;
import com.disver.spider.api.data.SpiderPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 */
public class Configuration implements SpiderConfiguration {
    /**
     * 数据转化器实例
     */
    private SpiderConverter converter;
    /**
     * 代理IP队列
     */
    private List<String> proxy = new ArrayList<>();
    /**
     * 过滤器队列
     */
    private List<SpiderFilter> filters = new ArrayList<>();
    /**
     * 数据管道队列
     */
    private List<SpiderPipeline> pipelines = new ArrayList<>();
    /**
     * 请求间隔
     */
    private Integer interval = 0;

    private boolean isLog = false;

    public Integer getInterval() {
        return interval;
    }

    /**
     * 获取到当前的数据转化器
     *
     * @return 数据转化器
     */
    @Override
    public SpiderConverter currentConverter() {
        return converter;
    }

    /**
     * 设置数据管道
     *
     * @param spiderPipeline 数据管道数组
     * @return 当前类
     */
    @Override
    public SpiderConfiguration pipeline(SpiderPipeline... spiderPipeline) {
        this.pipelines.addAll(Arrays.asList(spiderPipeline));
        return this;
    }

    /**
     * @return 获取当前所有的数据管道
     */
    public List<SpiderPipeline> getPipelines() {
        return pipelines;
    }

    /**
     * 设置数据转化器
     *
     * @param spiderConverter 数据转化器
     * @return 当前类
     */
    @Override
    public SpiderConfiguration converter(SpiderConverter spiderConverter) {
        this.converter = spiderConverter;
        return this;
    }

    /**
     * 设置代理IP
     * 参数形式为 IP:端口
     * 若不写端口默认为80
     *
     * @param strings 代理IP列表
     * @return
     */
    @Override
    public SpiderConfiguration proxy(String... strings) {
        proxy.addAll(Arrays.asList(strings));
        return this;
    }

    /**
     * @return 获取代理列表
     */
    @Override
    public List<String> proxies() {
        return proxy;
    }


    @Override
    public SpiderConfiguration interval(Integer integer) {
        this.interval = integer;
        return this;
    }


    @Override
    public List<SpiderFilter> filters() {
        return this.filters;
    }

    @Override
    public SpiderConfiguration log(boolean log) {
        this.isLog = log;
        return this;
    }

    @Override
    public boolean isLog() {
        return isLog;
    }

    @Override
    public SpiderConfiguration filter(SpiderFilter... spiderFilters) {
        this.filters.addAll(Arrays.asList(spiderFilters));
        return this;
    }
}
