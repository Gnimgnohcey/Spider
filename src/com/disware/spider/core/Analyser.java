package com.disware.spider.core;

import com.disver.spider.api.annotation.*;
import com.disver.spider.api.core.Context;
import com.disver.spider.api.core.SpiderBean;
import com.disware.spider.factory.SpiderFactory;
import com.disware.spider.listener.OnAnalyserListener;
import com.disware.spider.listener.OnConnectListener;
import com.disware.spider.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author 4everlynn
 * Create at 2018/6/19
 * 爬虫类分析器
 */
public class Analyser implements Callable<SpiderBean> {
    private SpiderBean target;
    private Context context;
    private Element element = null;
    private Elements elements = null;
    private final int ELEMENT = 200;
    private final int ELEMENTS = 400;
    private final int NONE = 500;
    private String baseDocument;
    private int min = -1;
    private Map<String, List<String>> params = new HashMap<>();
    private Field currentField;
    private Document document;
    private OnConnectListener connectListener;
    private Map<String, OnAnalyserListener> onAnalyserListenerMap;

    public Analyser setConnectListener(OnConnectListener connectListener) {
        this.connectListener = connectListener;
        return this;
    }

    public Analyser setOnAnalyserListener(Map<String, OnAnalyserListener> onAnalyserListenerMap) {
        this.onAnalyserListenerMap = onAnalyserListenerMap;
        return this;
    }

    public Analyser(SpiderBean target, Context context) {
        this.target = target;
        this.document = (Document) context.getDocument();
        this.context = context;
    }


    /**
     * 注入
     * 顺序为 Node -> Tag -> Attribute -- Value -> Text
     */
    private void inject() throws IllegalAccessException, InstantiationException {
        // Node 根据Id 或者 className 确定区域
        Class<? extends SpiderBean> targetClass = target.getClass();
        Node node = targetClass.getDeclaredAnnotation(Node.class);
        Field[] declaredFields = target.getClass().getDeclaredFields();
        // 遍历每个字段
        for (Field declaredField : declaredFields) {
            currentField = declaredField;
            dealNode(node, 1);
            // 只对有注解的字段进行处理
            int length = declaredField.getDeclaredAnnotations().length;
            if (length > 0) {
                System.err.println("Field " + declaredField.getName() + " has " + length + " annotations");
                dealField(declaredField);
            } else {
                System.err.println("Field " + declaredField.getName() + " has no annotations skip it");
            }
        }
    }

    /**
     * 处理字段注解
     *
     * @param declaredField
     */
    private void dealField(Field declaredField) throws IllegalAccessException {
        // 取当前字段上的 Node
        Node node = declaredField.getDeclaredAnnotation(Node.class);
        if (hasTarget(node)) {
            System.err.println("Field " + declaredField.getName() + " find node");
            dealNode(node, 0);
        }
        // 取当前字段上的 Tag
        Tag tag = declaredField.getDeclaredAnnotation(Tag.class);
        if (hasTarget(tag)) {
            System.err.println("Field " + declaredField.getName() + " find tag <" + tag.value() + ">");
            // 处理 Tag
            dealTag(tag);
        }
        // 取当前字段上的 Attribute
        Attribute attribute = declaredField.getDeclaredAnnotation(Attribute.class);
        if (hasTarget(attribute)) {
            System.err.println("Field " + declaredField.getName() + " find attribute " + attribute.value());
            // 处理 Attribute
            dealAttribute(attribute);
            // 取当前字段上的 Value
            Value value = declaredField.getDeclaredAnnotation(Value.class);
            if (hasTarget(value)) {
                System.err.println("Field " + declaredField.getName() + " find Value ");
                dealValue(value, attribute.value());
            }
        }
        // 取当前字段上的 Text
        Text text = declaredField.getDeclaredAnnotation(Text.class);
        if (hasTarget(text)) {
            System.err.println("Field " + declaredField.getName() + " find Text queryCss is '" + text.queryCss() + "'");
            dealText(text);
        }
        RestSpider restSpider = declaredField.getDeclaredAnnotation(RestSpider.class);
        // 如果当前字段需要请求接口获得数据
        if (hasTarget(restSpider)) {
            dealRestSpider(declaredField.getName());
        }
        // 分析完毕开始注入数据
        onResult();
        // 每一轮结束后都进行一次清空
        clearElement();
        clearElements();
    }

    private void dealRestSpider(String name) {
        params.computeIfAbsent(name, paramForField -> new ArrayList<>());
    }


    /**
     * 处理Node 注解
     * 根据Type 对应处理类或字段上的注解
     *
     * @param node
     * @param type
     */
    private void dealNode(Node node, int type) {
        if (!Strings.isNullOrEmpty(baseDocument) || type == 0) {
            if (node != null) {
                if (Strings.single(node.className(), node.id())) {
                    if (Strings.isNullOrEmpty(node.className())) {
                        elements = document.getElementsByClass(node.className());
                        if (type == 1 && checkElements(elements)) {
                            baseDocument = elements.toString();
                        }
                        clearElement();
                        System.err.println("className is '" + node.className() + "'");
                    } else {
                        element = document.getElementById(node.id());
                        if (type == 1 && hasTarget(element)) {
                            baseDocument = element.toString();
                        }
                        clearElements();
                        System.err.println("id is '" + node.id() + "'");
                    }
                } else {
                    throw new IllegalArgumentException("Node could only accept one argument");
                }
            }
        } else {
            element = new Element("target").append(baseDocument);
        }
    }

    private boolean hasTarget(Object target) {
        return target != null;
    }


    /**
     * 处理Tag注解
     *
     * @param tag
     */
    private void dealTag(Tag tag) {
        String value = tag.value();
        int current = current();
        // 如果当前是 id 生效
        if (current == ELEMENT) {
            elements = element.getElementsByTag(value);
            // 确保两个对象只有一个是存在实例的
            clearElement();
        } else if (current == ELEMENTS) {
            element = new Element("target");
            element.append(elements.toString());
            elements = element.getElementsByTag(value);
            // 确保两个对象只有一个是存在实例的
            clearElement();
        } else {
            // 如果两个都对象都为空则认为该对象没有使用Node注解
            elements = document.getElementsByTag(value);
        }
    }


    /**
     * 处理Attribute注解
     *
     * @param attribute
     */
    private void dealAttribute(Attribute attribute) {
        String value = attribute.value();
        int current = current();
        // 如果当前是 id 生效
        if (current == ELEMENT) {
            elements = element.getElementsByAttribute(value);
            // 确保两个对象只有一个是存在实例的
            clearElement();
        } else if (current == ELEMENTS) {
            element = new Element("target");
            element.append(elements.toString());
            elements = element.getElementsByAttribute(value);
            // 确保两个对象只有一个是存在实例的
            clearElement();
        } else {
            // 如果两个都对象都为空则认为该对象没有使用Node注解
            elements = document.getElementsByAttribute(value);
        }
    }

    /**
     * 处理Value注解
     *
     * @param value
     */
    private void dealValue(Value value, String key) {
        int current = current();
        // 如果当前是 id 生效
        if (current == ELEMENT) {
            System.out.println(element.attr(key));
        } else if (current == ELEMENTS) {
            List<String> eachAttr = elements.eachAttr(key);
            if (min == -1) {
                min = eachAttr.size();
            } else if (min > eachAttr.size() && eachAttr.size() > 0) {
                min = eachAttr.size();
            }
            params.put(currentField.getName(), eachAttr);
        }
    }

    /**
     * 处理Text注解
     *
     * @param text
     */
    private void dealText(Text text) {
        String queryCss = text.queryCss();
        if (Strings.isNullOrEmpty(queryCss)) {
            int current = current();
            if (current == ELEMENT) {
                elements = element.select(queryCss);
                clearElement();
            } else if (current == ELEMENTS) {
                element = new Element("target");
                element.append(elements.toString());
                elements = element.select(queryCss);
                clearElement();
            } else {
                // 如果两个都对象都为空则认为该对象没有使用Node注解
                elements = document.select(queryCss);
            }
            List<String> eachText = elements.eachText();
            params.put(currentField.getName(), eachText);
            if (min == -1) {
                min = eachText.size();
            } else if (min > eachText.size() && eachText.size() > 0) {
                min = eachText.size();
            }
        } else {
            throw new NullPointerException("queryCss can not be empty");
        }
    }

    /**
     * 获取当前有效的对象
     *
     * @return
     */
    private int current() {
        if (hasTarget(element)) {
            return ELEMENT;
        } else if (checkElements(elements)) {
            return ELEMENTS;
        }
        return NONE;
    }

    /**
     * 清空Element
     */
    private void clearElement() {
        element = null;
    }

    private void clearElements() {
        elements = null;
    }


    private boolean checkElements(Elements elements) {
        return elements != null && elements.size() > 0;
    }

    /**
     * 此处总是对Elements进行分析
     * 在经过注解处理后Element总为空
     */
    private void onResult() {
        currentField.setAccessible(true);
        try {
            List<String> currentParams = params.get(currentField.getName());
            currentField.set(target, currentParams);
            InnerSpider innerSpider = currentField.getDeclaredAnnotation(InnerSpider.class);
            // 如果该字段也是一个爬虫类
            OnAnalyserListener onAnalyserListener = null;
            if (onAnalyserListenerMap != null) {
                onAnalyserListener = onAnalyserListenerMap.get(currentField.getName());
            }
            if (onAnalyserListener != null) {
                currentParams = onAnalyserListener.preAnalyse(context, currentParams);
            }
            if (hasTarget(innerSpider)) {
                SpiderBean spiderBean = innerSpider.value().newInstance();
                for (int i = 0; i < currentParams.size(); i++) {
                    String currentParam = currentParams.get(i);
                    if (onAnalyserListener != null) {
                        spiderBean = onAnalyserListener.process(context, spiderBean, params, i);
                    }
                    if (connectListener != null) {
                        currentParam = connectListener.process(currentParam);
                    }
                    SpiderFactory.superSpider(
                            context.target(currentParam).connect())
                            .analyse(spiderBean);
                    if (onAnalyserListener != null) {
                        spiderBean = onAnalyserListener.process(context, spiderBean, params, i);
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        currentField.setAccessible(false);
    }

    /**
     * 多线程调用
     *
     * @return 注入完毕的SpiderBean
     * @throws Exception
     */
    @Override
    public SpiderBean call() throws Exception {
        // 开始注入
        inject();
        // 返回目标
        return target;
    }
}
