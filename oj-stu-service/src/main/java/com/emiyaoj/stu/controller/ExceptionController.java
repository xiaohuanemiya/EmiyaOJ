package com.emiyaoj.stu.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.common.exception.BadRequestException;
import com.emiyaoj.common.exception.BaseException;
import com.emiyaoj.common.exception.CustomerAuthenticationException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * 学生端全局异常处理器
 */
@Slf4j
@ControllerAdvice
@RestController
public class ExceptionController {
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseResult<?> badRequest(BadRequestException e) {
        log.error("请求错误: {}", e.getMessage());
        return ResponseResult.fail(500, e.getMessage());
    }
    
    @ExceptionHandler(BaseException.class)
    public ResponseResult<?> base(BaseException e) {
        log.error("业务异常: {}", e.getMessage());
        return ResponseResult.fail(e.getResultEnum().getCode(), e.getResultEnum().message());
    }
    
    @ExceptionHandler(CustomerAuthenticationException.class)
    public ResponseResult<?> customerAuth(CustomerAuthenticationException e) {
        log.error("认证异常: {}", e.getMessage());
        return ResponseResult.fail(401, e.getMessage());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<?> dtoValidationException(ValidationException e) {
        log.error("参数验证错误: {}", e.getMessage());
        return ResponseResult.fail(400, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> methodValidationException(ValidationException e) {
        log.error("方法参数验证错误: {}", e.getMessage());
        return ResponseResult.fail(400, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult<?> methodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error("请求方法不支持: {}", e.getMessage());
        return ResponseResult.fail(e.getStatusCode().value(), e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseResult<?> mediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.error("请求媒体类型不支持: {}", e.getMessage());
        return ResponseResult.fail(e.getStatusCode().value(), e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult<?> accessDenied() {
        log.error("访问被拒绝");
        return ResponseResult.fail(403, "无权限");
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult<?> authException(AuthenticationException e) {
        log.error("认证失败: {}", e.getMessage());
        return ResponseResult.fail(401, "认证失败: " + e.getMessage());
    }
    
    @ExceptionHandler(MultipartException.class)
    public ResponseResult<?> multipart() {
        log.error("文件上传失败");
        return ResponseResult.fail(500, "文件上传失败");
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseResult<?> ioe(IOException e) {
        log.error("IO异常: {}", e.getMessage());
        return ResponseResult.fail(500, "IO异常");
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<?> runtimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage(), e);
        return ResponseResult.fail(500, e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> exception(Exception e) {
        log.error("未知异常: {}", e.getMessage(), e);
        return ResponseResult.fail(500, e.getMessage());
    }
}

