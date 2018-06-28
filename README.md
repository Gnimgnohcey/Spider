# Spider
## 介绍
> Spider是一个多线程，多管道，多过滤器的高度可自定义爬虫框架是对spider-api的实现，
框架开发基于Java的注解和Jsoup，可以用很少量的代码写一个多线程爬虫，支持数据转化器，代理IP,请求Rest接口等基础功能
并实现了文件流下载器，每一个爬虫爬取数据的过程都在单独的线程中执行，待爬虫类注入完成后主动返回主线程进行数据同步，
Spider支持指定线程池大小，并且数据下载的过程是并发的。
## API文档
- [Java Doc API ](http://www.diswares.com/spider-api)
## 架构
> SpiderEngine 是爬虫的主引擎，也可以认为是整个爬虫的入口
> ![架构图](http://www.diswares.com/Spider架构.jpg)
## 多线程架构
> ReticularSpiders是Spider从0.12版本后开始支持的，它使Spider Engine能够在多个线程中并发运行，由于Spider Engine本身就是多线程的，所以使用ReticularSpiders后Spider Engine内部的线程将会成为子线程
> ![多线程爬虫架构图](http://www.diswares.com/多线程爬虫架构.png)
## 爬虫示例
  - Hello Spider
    - 配置 Spider
      ```java
        public class HelloSpider {
          public static void main(String[] args) {
              new Spider(new SpiderContext()
                      // target url
                      .target("https://github.com/4everlynn/Spider/")
                      // Header
                      .header(new SpiderHeader()
                              .include("Content-Type", "text/html; charset=utf-8")
                              .include("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5)" +
                                      " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36"))
                      // add downloader
                      // also can use custom Downloader 
                      .downloader(new SpiderDownloader())
                      .configuration(new Configuration()
                              // proxies
                              .proxy("127.0.0.1:80", "10.0.0.1:8080")
                              // data converter 
                              // also can use custom Converter 
                              .converter(new JsonConverter())
                              // add pipeline
                              // also can use custom Pipeline 
                              .pipeline(new FilePipeline("targetFile"))
                              .filter(new SimpleFilter()
                                      .include(".*?png")
                                      .include(".*?jpg"))
                              // request interval 
                              .interval(100))
                      // connect to target
                      .connect()
              );
            }
        }
     ```
  > 由上可见，高度可自定义的代价就是Spider的配置过程是一个比较复杂的过程，为了减少用户在配置上花费的时间，Spider提供了一套默认的配置，这套配置可以从SpiderFactory中获得
