package com.disware.spider.core;

import com.disver.spider.api.core.*;
import com.disver.spider.api.data.SpiderData;
import com.disver.spider.api.data.SpiderPipeline;
import com.disware.spider.data.SpiderDataResult;
import com.disware.spider.listener.OnAnalyserListener;
import com.disware.spider.listener.OnConnectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 4everlynn
 * Create at 2018/6/7
 * 爬虫引擎实现类
 */
public class Spider implements SpiderEngine {
    private Configuration configuration;
    private boolean integrity;
    private Context context;
    private Integer pool = 1;
    private ExecutorService executorService;
    private List<Future<SpiderBean>> futures = new ArrayList<>();
    private OnConnectListener connectListener;
    private Map<String, OnAnalyserListener> onAnalyserListenerMap = new HashMap<>();


    public Spider(Context context) {
        this.context = context;
        onCreate(context);
    }

    @Override
    public SpiderData analyse(SpiderBean... spiderBeans) {
        // 实例化结果
        SpiderData spiderData = new SpiderDataResult(this);
        // 遍历进行分析
        for (SpiderBean spiderBean : spiderBeans) {
            // 判断配置类是否实例化
            if (configuration != null) {
                // 拿到过滤器队列
                List<SpiderFilter> filters = configuration.filters();
                // 判断过滤器是否存在一个以上
                if (filters.size() > 0) {
                    for (SpiderFilter filter : filters) {
                        // 执行过滤
                        if (filter.filter(spiderBean)) {
                            // 加入多线程队列 转移逻辑到 Analyser 进行异步注入
                            start(new Analyser(spiderBean, context)
                                    .setOnAnalyserListener(onAnalyserListenerMap)
                                    .setConnectListener(connectListener));
                        }
                    }
                } else {
                    // 加入多线程队列 转移逻辑到 Analyser 进行异步注入
                    start(new Analyser(spiderBean, context)
                            .setOnAnalyserListener(onAnalyserListenerMap)
                            .setConnectListener(connectListener));
                }
            } else {
                // 加入多线程队列 转移逻辑到 Analyser 进行异步注入
                start(new Analyser(spiderBean, context)
                        .setOnAnalyserListener(onAnalyserListenerMap)
                        .setConnectListener(connectListener));
            }
        }
        // 分析完成后执行生命周期函数
        onDestroy();
        // 处理结果
        result(spiderData);
        // 返回结果
        return spiderData;
    }

    public Spider innerSpiderUrlListener(OnConnectListener listener) {
        this.connectListener = listener;
        return this;
    }


    public Spider analyserListener(String fieldName, OnAnalyserListener onAnalyserListener) {
        onAnalyserListenerMap.put(fieldName, onAnalyserListener);
        return this;
    }

    /**
     * @param analyser
     */
    private void start(Analyser analyser) {
        // 分配线程进行处理
        futures.add(executorService.submit(analyser));
    }

    /**
     * 同步数据
     *
     * @param spiderData
     */
    private void result(SpiderData spiderData) {
        for (Future<SpiderBean> future : futures) {
            try {
                SpiderBean spiderBeans = future.get();
                replace(spiderData, spiderBeans);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void replace(SpiderData spiderData, SpiderBean spiderBean) {
        // 数据流管道
        List<SpiderPipeline> pipelines = configuration.getPipelines();
        if (pipelines.size() > 0) {
            for (SpiderPipeline pipeline : pipelines) {
                SpiderFilter[] filters = pipeline.filters();
                if (null != filters && filters.length > 0) {
                    for (SpiderFilter filter : filters) {
                        if (filter.filter(spiderBean)) {
                            spiderBean = pipeline.preDeal(context, spiderBean);
                            pipeline.assemblyLine(context, spiderBean);
                        }
                    }
                } else {
                    spiderBean = pipeline.preDeal(context, spiderBean);
                    pipeline.assemblyLine(context, spiderBean);
                }
            }
        }
        if (null != configuration.currentConverter()) {
            spiderData.reset(spiderBean.token(), configuration.currentConverter().convert(spiderBean));
        } else {
            spiderData.reset(spiderBean.token(), spiderBean);
        }
    }

    /**
     * 暂停主线程
     *
     * @param time
     */
    public void pause(Integer time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回当前上下文
     *
     * @return
     */
    @Override
    public Context getContext() {
        return context;
    }

    /**
     * 指定线程池大小
     *
     * @param integer
     * @return
     */
    @Override
    public Spider pool(Integer integer) {
        executorService = Executors.newFixedThreadPool(pool);
        return this;
    }

    /**
     * 是否只抓取完整数据
     *
     * @param integrity
     * @return
     */
    @Override
    public SpiderEngine integrity(boolean integrity) {
        this.integrity = integrity;
        return this;
    }

    /**
     * 设置配置文件
     *
     * @param spiderConfiguration
     * @return
     */
    @Override
    public SpiderEngine configuration(SpiderConfiguration spiderConfiguration) {
        this.configuration = (Configuration) spiderConfiguration;
        return this;
    }

    /**
     * 生命周期函数，爬虫主线程创建时调用
     *
     * @param context
     */
    @Override
    public void onCreate(Context context) {
        if (context != null) {
            configuration(context.getConfiguration());
        } else {
            throw new NullPointerException("Context is undefined");
        }
        // 指定线程池大小
        pool(pool);
        // 指定请求间隔时间
        pause(configuration.getInterval());
    }

    /**
     * 生命周期函数，爬虫主线程analyse方法分配任务结束后调用
     */
    @Override
    public void onDestroy() {
        executorService.shutdown();
    }
}
