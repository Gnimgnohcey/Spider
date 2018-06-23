package com.disware.spider.core;

import com.disver.spider.api.core.Context;
import com.disver.spider.api.core.SpiderConfiguration;
import com.disver.spider.api.io.Downloader;
import com.disware.spider.util.Strings;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 */
public class SpiderContext implements Context {
    private Document document;
    private Connection connect;
    private Map<String, String> headers = new HashMap<>();
    private SpiderConfiguration configuration;
    private String target;
    private Downloader downloader;

    public SpiderContext(String url, Map<String, String> headers) {
        this.target = url;
        this.headers = headers;
    }

    private void proxy() {
        if (null != configuration) {
            List<String> proxies = configuration.proxies();
            // 如果指定了 代理IP
            if (proxies != null && proxies.size() > 0) {
                int size = proxies.size();
                String randomProxy = proxies.get((int) Math.floor(Math.random() * size));
                System.err.println("Proxy Address is " + randomProxy);
                String[] proxyArray = randomProxy.split(":");
                if (proxyArray.length == 2) {
                    // 设置代理IP
                    connect.proxy(proxyArray[0], Integer.valueOf(proxyArray[1]));
                } else {
                    // 如果没有指定端口 默认端口 80
                    connect.proxy(proxyArray[0], 80);
                }
            }
        }
    }


    public SpiderContext() {
        this(null);
    }

    public SpiderContext(String url) {
        this(url, null);
    }

    @Override
    public Document getDocument() {
        if (document == null) {
            try {
                document = connect.get();
                return document;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return document;
        }
        return null;
    }

    @Override
    public String getTarget() {
        return getDocument().location();
    }

    @Override
    public Connection getConnection() {
        return connect;
    }

    @Override
    public Context target(String url) {
        this.target = url;
        return this;
    }

    @Override
    public Context header(Map<String, String> map) {
        this.headers = map;
        return this;
    }

    @Override
    public Context downloader(Downloader downloader) {
        this.downloader = downloader;
        if (headers != null) {
            downloader.headers(headers);
        }
        return this;
    }


    @Override
    public Map<String, String> customHeaders() {
        return headers;
    }

    @Override
    public Context configuration(SpiderConfiguration spiderConfiguration) {
        this.configuration = spiderConfiguration;
        return this;
    }

    @Override
    public SpiderConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public Context connect() {
        if (Strings.isNullOrEmpty(target)) {
            connect = Jsoup.connect(target);
            try {
                document = connect.get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            proxy();
            if (null != headers) {
                connect.headers(headers);
            }
        }
        return this;
    }

    @Override
    public Downloader getDownloader() {
        return downloader;
    }
}
