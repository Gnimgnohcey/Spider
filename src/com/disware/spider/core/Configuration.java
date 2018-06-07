package com.disware.spider.core;

import com.disver.spider.api.core.SpiderConfiguration;
import com.disver.spider.api.core.SpiderFilter;
import com.disver.spider.api.data.SpiderConverter;
import com.disver.spider.api.data.SpiderPipeline;
import com.disver.spider.api.io.Downloader;

import java.util.Arrays;
import java.util.List;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 */
public class Configuration implements SpiderConfiguration {
    private SpiderConverter converter;
    private List<String> proxy;
    private List<SpiderFilter> filters;
    private List<SpiderPipeline> pipelines;
    private Integer interval;
    private Downloader downloader;

    @Override
    public SpiderConfiguration pipeline(SpiderPipeline spiderPipeline) {
        this.pipelines.add(spiderPipeline);
        return this;
    }

    @Override
    public SpiderConfiguration converter(SpiderConverter spiderConverter) {
        this.converter = spiderConverter;
        return this;
    }

    @Override
    public SpiderConfiguration proxy(String... strings) {
        proxy.addAll(Arrays.asList(strings));
        return this;
    }

    @Override
    public SpiderConfiguration interval(Integer integer) {
        this.interval = integer;
        return this;
    }

    @Override
    public SpiderConfiguration downloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    @Override
    public SpiderConfiguration filter(SpiderFilter... spiderFilters) {
        this.filters.addAll(Arrays.asList(spiderFilters));
        return this;
    }
}
