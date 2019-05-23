package com.tjlcast.SmartRpc.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author by tangjialiang
 *         时间 2019/5/22.
 *         说明 ...
 */
public class StringUtil {

    private StringUtil() {}

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmtpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmtpty(str);
    }

    /**
     * 根据字符进行字符串分割
     * @param str
     * @param separator
     * @return
     */
    public static String[] split(String str, String separator) {
        return StringUtils.splitByWholeSeparator(str, separator);
    }
}
