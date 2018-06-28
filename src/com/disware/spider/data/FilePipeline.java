package com.disware.spider.data;

import com.disver.spider.api.core.Context;
import com.disver.spider.api.core.SpiderBean;
import com.disver.spider.api.core.SpiderFilter;
import com.disver.spider.api.data.SpiderPipeline;
import com.disware.spider.io.FileManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 4everlynn
 * Create at 2018/6/21
 * 官方数据管道
 * 用于输出日志文件
 */
public class FilePipeline implements SpiderPipeline {
    private String url;

    public FilePipeline(String url) {
        this.url = url;
    }


    @Override
    public SpiderBean preDeal(Context context, SpiderBean bean) {
        return bean;
    }

    @Override
    public SpiderPipeline assemblyLine(Context context, SpiderBean bean) {
        System.err.println(getClass().getName() + " start working");
        StringBuilder builder = new StringBuilder();
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if ("java.util.List".equals(declaredField.getType().getName())) {
                builder.append(" [").append(declaredField.getName().toUpperCase()).append("]\n");
                try {
                    List<String> target = (List<String>) declaredField.get(bean);
                    if (null != target) {
                        builder.append("")
                                .append(target.toString()
                                        .replaceAll(",", "\n")
                                        .replaceAll("\\[", " ")
                                        .replaceAll("\\]", ""));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            declaredField.setAccessible(false);
            builder.append("\n");
        }
        FileManager.saveText(url + File.separator + bean.token() + ".txt", builder.toString());
        return this;
    }

    @Override
    public SpiderFilter[] filters() {
        return new SpiderFilter[0];
    }

    @Override
    public SpiderPipeline filter(SpiderFilter... spiderFilters) {
        return null;
    }
}
