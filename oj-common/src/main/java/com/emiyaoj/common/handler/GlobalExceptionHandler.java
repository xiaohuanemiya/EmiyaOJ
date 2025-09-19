package com.emiyaoj.common.handler;

import com.emiyaoj.common.constant.ResultEnum;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获所有异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult ex(Exception ex){

        // 如果是访问拒绝异常，不处理，让SecurityConfig中配置的处理器处理
        if(ex instanceof AccessDeniedException) {

            throw (AccessDeniedException)ex;

        }

        log.error("全局异常信息：{}", ex.getMessage());
        return ResponseResult.fail(StringUtils.hasLength(ex.getMessage()) ? ex.getMessage() : "操作失败");
    }

    /**
     * 捕获业务异常
     */
    @ExceptionHandler
    public ResponseResult exceptionHandler(BaseException ex){
        log.error("业务异常信息：{}", ex.getResultEnum().message());
        return ResponseResult.fail(ex.getResultEnum().message());
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler
    public ResponseResult exceptionHandler(SQLIntegrityConstraintViolationException ex){
        // 错误信息：Duplicate entry 'zhaosi' for key 'employee.idx_username' -- > 用户ID重复
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + ResultEnum.USER_NAME_HAS_EXISTED.message();
            return ResponseResult.fail(msg);
        }else {
            return ResponseResult.fail(ResultEnum.INTERNAL_SERVER_ERROR.message());
        }

    }
}
