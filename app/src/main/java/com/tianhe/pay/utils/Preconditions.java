package com.tianhe.pay.utils;

import java.util.Collection;

public class Preconditions {

    public static void checkState(boolean state) {
        if (!state) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean state, String format, Object... args) {
        if (!state) {
            throw new IllegalStateException(String.format(format, args));
        }
    }

    public static <T extends Collection> T enforcesSize(T t, int expectedSize, String name) {
        if(nonNull(t, name).size() != expectedSize) {
            throw new IllegalArgumentException(name + " must be of size " + expectedSize + ", not " + t.size());
        }
        return null;
    }

    public static <T extends CharSequence> T nonBlank(T var0, String name) {
        if(Strings.isBlank(name)) {
            throw new IllegalArgumentException(name + " must not be blank");
        } else {
            return var0;
        }
    }

    public static <T extends Collection> T nonEmpty(T t, String name) {
        if((nonNull(t, name)).isEmpty()) {
            throw new IllegalArgumentException(name + " must not be empty");
        } else {
            return t;
        }
    }

    public static <T> T nonNull(T t, String name) {
        if(t == null) {
            throw new NullPointerException(name + " must not be null");
        } else {
            return t;
        }
    }
}
