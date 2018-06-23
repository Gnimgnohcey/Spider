package com.disware.spider.core;

import com.disver.spider.api.core.SpiderBean;
import com.disver.spider.api.core.SpiderFilter;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 4everlynn
 * Create at 2018/6/8
 * 实现了一个简单的正则过滤器，用户也可以自定义过滤器
 */
public class SimpleFilter extends HashSet<String> implements SpiderFilter {
    @Override
    public SpiderFilter include(String... patterns) {
        for (String pattern : patterns) {
            add(pattern);
        }
        return this;
    }

    @Override
    public SpiderFilter exclude(String... patterns) {
        for (String pattern : patterns) {
            remove(pattern);
        }
        return this;
    }

    @Override
    public boolean filter(SpiderBean spiderBean) {
        Pattern pattern;
        for (String current : this) {
            pattern = Pattern.compile(current);
            Matcher matcher = pattern.matcher(spiderBean.token());
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}
