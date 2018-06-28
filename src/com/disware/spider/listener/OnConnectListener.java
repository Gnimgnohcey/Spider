package com.disware.spider.listener;

/**
 * @author 4everlynn
 * Create at 2018/6/22
 * InnerSpider内部连接URL时对URL的回调
 * 可用于处理URL
 */
public interface OnConnectListener {
    /**
     * 连接过程对URL的监听
     *
     * @param url 当前url
     * @return 处理后的url
     */
    String process(String url);
}
