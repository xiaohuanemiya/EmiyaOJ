package com.emiyaoj.service.config;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * <h1>调试类</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Slf4j
@UtilityClass
public class Debug {
    @Value("${debug.enabled:true}")
    private boolean Enabled;
    
    public void trace(boolean condition, Object object) {
        if (Enabled && condition) {
            log.trace("{}", object);
        }
    }
    
    public void trace(boolean condition, String regex, Object... objects) {
        if (Enabled && condition) {
            log.trace(regex, objects);
        }
    }
    
    public void debug(boolean condition, Object object) {
        if (Enabled && condition) {
            log.debug("{}", object);
        }
    }
    
    public void debug(boolean condition, String regex, Object... objects) {
        if (Enabled && condition) {
            log.debug(regex, objects);
        }
    }
    
    public void info(boolean condition, Object object) {
        if (Enabled && condition) {
            log.info("{}", object);
        }
    }
    
    public void info(boolean condition, String regex, Object... objects) {
        if (Enabled && condition) {
            log.info(regex, objects);
        }
    }
    
    public void warn(boolean condition, Object object) {
        if (Enabled && condition) {
            log.warn("{}", object);
        }
    }
    
    public void warn(boolean condition, String regex, Object... objects) {
        if (Enabled && condition) {
            log.warn(regex, objects);
        }
    }
    
    public void error(boolean condition, Object object) {
        if (Enabled && condition) {
            log.error("{}", object);
        }
    }
    
    public void error(boolean condition, String regex, Object... objects) {
        if (Enabled && condition) {
            log.error(regex, objects);
        }
    }
}
