package com.yunniao.appiumtest.utils;

import java.util.HashMap;

/**
 * Created by MrBu on 2016/1/19.
 */
public class KeyValueUtil {
    private static HashMap<String, Object> map = new HashMap<>();

    public static void put(String key, Object value) {
        map.put(key, value);
    }

    public static <T> T get(String key) {
        return (T) map.get(key);
    }

    public static void clear() {
        map.clear();
    }

}
