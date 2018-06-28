package com.disware.spider.util;

/**
 * @author 4everlynn
 * Create at 2018/6/19
 * 文本工具类
 */
public class Strings {
    /**
     * 判断文本参数是否都不为空
     *
     * @param strings 文本参数数组
     * @return 结果
     */
    public static boolean isNullOrEmpty(String... strings) {
        for (String string : strings) {
            if ("".equals(string) || null == string) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断所有的文本参数数组是否只存在一个实例
     *
     * @param strings 文本参数数组
     * @return 结果
     */
    public static boolean single(String... strings) {
        int count = 0;
        for (String string : strings) {
            if (!("".equals(string) || null == string)) {
                count++;
            }
        }
        return count == 1;
    }
}
