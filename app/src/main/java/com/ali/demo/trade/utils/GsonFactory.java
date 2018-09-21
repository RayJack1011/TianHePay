package com.ali.demo.trade.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory {

    public static Gson getGson() {
        return GsonFactory.GsonHolder.gson;
    }

    private static class GsonHolder {
        private static Gson gson;

        static {
            gson = new GsonBuilder().create();
        }

        private GsonHolder() {
        }
    }
}
