package com.tencent.common;

import java.util.Random;

public class RandomStringGenerator {

    private RandomStringGenerator() {
    }

    /**
     * 获取一定长度的随机字符串
     * @param length 随机字符串的长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        final int bound = base.length();
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(bound);
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
