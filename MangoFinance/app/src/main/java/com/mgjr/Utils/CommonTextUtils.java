package com.mgjr.Utils;

/**
 * Created by Administrator on 2016/9/26.
 */

public class CommonTextUtils {
    /**
     * 功能：判断一个字符串是否包含特殊字符
     *
     * @param string
     *            要判断的字符串
     * @return false 提供的参数string不包含特殊字符 true 提供的参数string包含特殊字符
     */
    public static boolean isConSpeCharacters(String string) {
        if (string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0) {
            // 如果不包含特殊字符
            return false;
        }
        return true;
    }
}
