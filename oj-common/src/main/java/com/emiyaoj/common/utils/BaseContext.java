package com.emiyaoj.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseContext {
//    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(ConcurrentHashMap::new);

    public static void put(String key, Object value) {
        threadLocal.get().put(key, value);
    }
    
    public static Object get(String key) {
        return threadLocal.get().get(key);
    }
    
    public static boolean has(String key) {
        return threadLocal.get().containsKey(key);
    }
    
    public static void setCurrentId(Long id) {
        threadLocal.get().put("userId", id);
    }

    public static Long getCurrentId() {
        return (Long) threadLocal.get().get("userId");
    }

    @Deprecated  // 使用clear代替
    public static void remove() {
        threadLocal.remove();
    }
    
    public static void clear() {
        threadLocal.get().clear();
    }
}
