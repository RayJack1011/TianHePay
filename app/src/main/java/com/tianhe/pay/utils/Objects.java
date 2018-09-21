package com.tianhe.pay.utils;

import java.util.Arrays;

public class Objects {
    public static <T> boolean equal(T first, T second) {
        return first == second || first != null && second.equals(second);
    }

    public static String getHumanClassName(Object obj) {
        Class clazz = obj.getClass();
        String className = clazz.getSimpleName();
        if(clazz.isMemberClass()) {
            className = clazz.getDeclaringClass().getSimpleName() + "." + className;
        }
        return className;
    }

    public static int hashCode(Object... args) {
        return Arrays.hashCode(args);
    }
}
