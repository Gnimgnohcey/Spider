package com.disware.spider.listener;

/**
 * @author 4everlynn
 * Create at 2018/6/24
 * 这个接口标示了该类是个多线程类
 */
public interface Reticular<T> {
    /**
     * 多线程爬虫入口
     *
     * @param args 用户自定义参数
     * @return 用户自定义结果
     */
    T start(String... args);
}
