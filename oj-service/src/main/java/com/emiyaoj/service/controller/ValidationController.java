package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
@RestController
public class ValidationController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<?> dtoValidationError(ValidationException e) {
//        log.warn("DTO参数验证错误：{}", e.getMessage());
        return ResponseResult.fail(400, e.getCause().getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> methodValidationError(ValidationException e) {
        return ResponseResult.fail(400, e.getCause().getMessage());
    }
}
