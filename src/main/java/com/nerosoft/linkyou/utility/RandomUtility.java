package com.nerosoft.linkyou.utility;

/**
 * 随机数工具类
 * 
 * 提供各种随机数生成方法，包括随机字符串生成等。
 * 
 * @author nerosoft
 * @version 1.0
 */
public class RandomUtility {
    private static final java.util.Random random = new java.util.Random();

    /**
     * 生成指定长度的随机字符串
     * 
     * 随机字符串包含大小写字母和数字字符
     * 
     * @param length 随机字符串的长度
     * @return 指定长度的随机字符串
     */
    public static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
