package com.emiyaoj.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BeanUtil {
    public static <T, S> List<T> copyToList(List<S> source, Class<T> targetClass) {
        if (ObjectUtil.isEmpty(source)) {
            return Collections.emptyList();
        }
        if (targetClass == null) {
            throw new IllegalArgumentException("targetClass must not be null");
        }
        return source.stream()
                .filter(Objects::nonNull)
                .map(item -> {
                    try {
                        T target = targetClass.getDeclaredConstructor().newInstance();
                        BeanUtils.copyProperties(item, target);
                        return target;
                    } catch (Exception e) {
                        throw new IllegalStateException("Failed to copy to target class: " + targetClass.getName(), e);
                    }
                })
                .collect(Collectors.toList());
    }
}
