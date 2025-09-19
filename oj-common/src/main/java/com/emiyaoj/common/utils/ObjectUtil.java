package com.emiyaoj.common.utils;

public class ObjectUtil {
    public static Boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).trim().isEmpty();
        }
        if (obj instanceof java.util.Collection) {
            return ((java.util.Collection<?>) obj).isEmpty();
        }
        if (obj instanceof java.util.Map) {
            return ((java.util.Map<?, ?>) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(obj) == 0;
        }
        return false;
    }
    public static Boolean isNull(Object obj) {
        return obj == null;
    }
}
