package com.emiyaoj.common.domain;

import lombok.Data;

@Data
public class ResponseResult<T> {
    private int code;
    private String msg;
    private T data;

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    // 静态方法
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "Success", data);
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(200, "Success", null);
    }

    public static <T> ResponseResult<T> fail(String msg) {
        return new ResponseResult<>(500, msg, null);
    }

    public static <T> ResponseResult<T> fail(int code, String msg) {
        return new ResponseResult<>(code, msg, null);
    }
}
