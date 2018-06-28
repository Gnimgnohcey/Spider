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
### Hello Spider
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
 ### SpiderFactory
 ```java
  // 使用
  Spider spider = SpiderFactory.defaultSpider("targetURL","logFilePath");
  // 源代码
  /**
     * 为了节省用户配置的时间，提供一套约定俗成的配置
     * 这个默认的爬虫配置了官方的数据流管道FilePipeline
     *
     * @param target 目标网址
     * @param path   日志文件存放位置
     * @return 默认配置的Spider
     */
    public static Spider defaultSpider(String target, String path) {
        return new Spider(new SpiderContext()
                .target(target)
                .downloader(new SpiderDownloader())
                .header(new SpiderHeader().include("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36")
                        .include("Content-Type", "text/html"))
                .configuration(new Configuration()
                        .pipeline(new FilePipeline(path))
                        .interval(100))
                .connect())
                .pool(10);
    }
 ```
## 注解
|名称|作用|作用类型|
|:-:|:-:|:-:|
|Node|根据DOM的ID或CLASS指定爬取目标的区域|类或字段|
|Tag|根据HTML标签寻找目标集合|字段|
|Attribute|根据DOM所拥有的属性寻找目标集合|字段|
|Value|取出Attribute的值形成一个集合，该注解需要和Attribute配合使用|字段|
|Text|使用CSS选择器取出DOM的文本内容|字段|
|InnerSpider|InnerSpider是Analyser的子线程，可以十分方便进行双层爬虫|字段|
|RestSpider|标示当前字段需要请求REST接口，Spider引擎无需对此字段处理，由用户处理|字段|
