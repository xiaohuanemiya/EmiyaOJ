package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
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

@Slf4j
@ControllerAdvice
@RestController
public class ExceptionController {
    // 主要是这个参数验证处理，其他的是挑可能用到的写
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<?> dtoValidationException(ValidationException e) {
//        log.warn("DTO参数验证错误：{}", e.getMessage());
        return ResponseResult.fail(400, e.getCause().getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> methodValidationException(ValidationException e) {
        return ResponseResult.fail(400, e.getCause().getMessage());
    }
    
    // 请求方法不支持
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult<?> methodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseResult.fail(e.getStatusCode().value(), e.getCause().getMessage());
    }
    
    // 请求媒体类型不支持
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseResult<?> mediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        return ResponseResult.fail(e.getStatusCode().value(), e.getCause().getMessage());
    }
    
    // 访问拒绝
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult<?> accessDenied() {
        return ResponseResult.fail(403, "无权限");
    }
    
    // 认证失败
    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult<?> authException() {
        return ResponseResult.fail(401, "认证失败");
    }
    
    // 文件上传异常
    @ExceptionHandler(MultipartException.class)
    public ResponseResult<?> multipart() {
        return ResponseResult.fail(500, "文件上传失败");
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseResult<?> ioe() {
        return ResponseResult.fail(500, "IO异常");
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> exception(Exception e) {
        return ResponseResult.fail(500, e.getMessage());
    }
}
