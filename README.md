# Spider
> Spider是一个多线程，多管道，多过滤器的高度可自定义爬虫框架是对spider-api的实现，
框架开发基于Java的注解和Jsoup，可以用很少量的代码写一个多线程爬虫，支持数据转化器，代理IP,请求Rest接口等基础功能
并实现了文件流下载器，每一个爬虫爬取数据的过程都在单独的线程中执行，待爬虫类注入完成后主动返回主线程进行数据同步，
Spider支持指定线程池大小，并且数据下载的过程是并发的。

- [Java Doc API ](http://www.diswares.com/spider-api)
> ![架构图](http://www.diswares.com/Spider架构.jpg)
> ![多线程爬虫架构图](http://www.diswares.com/多线程爬虫架构.png)
