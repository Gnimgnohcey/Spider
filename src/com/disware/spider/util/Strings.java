package com.disware.spider.util;

/**
 * @author 4everlynn
 * Create at 2018/6/19
 */
public class Strings {
    public static boolean isNullOrEmpty(String... strings) {
        for (String string : strings) {
            if ("".equals(string) || null == string) {
                return false;
            }
        }
        return true;
    }

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
