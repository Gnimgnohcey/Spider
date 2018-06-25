package com.disware.spider.listener;

/**
 * @author 4everlynn
 * Create at 2018/6/24
 */
public interface  Reticular<T> {
    /**
     * 多线程爬虫入口
     *
     * @param args
     */
    T start(String... args);
}
