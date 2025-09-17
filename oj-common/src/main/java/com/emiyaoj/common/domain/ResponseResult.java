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

    public ResponseResult<T> success(T data) {
        this.code = 200;
        this.msg = "Success";
        this.data = data;
        return this;
    }
    public ResponseResult<T> success() {
        this.code = 200;
        this.msg = "Success";
        this.data = null;
        return this;
    }
    public ResponseResult<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
        return this;
    }
}
