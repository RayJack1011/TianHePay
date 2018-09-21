package com.ali.utils;

public class StringUtils {
    public static boolean isEmpty(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }
        for(int i = 0; i < value.length(); ++i) {
            if(!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
