package com.emiyaoj.common.exception;

import com.emiyaoj.common.constant.ResultEnum;

public class BaseException extends RuntimeException {
    private final ResultEnum resultEnum;

    public BaseException(ResultEnum resultEnum) {
        super(resultEnum.message());
        this.resultEnum = resultEnum;
    }

    public ResultEnum getResultEnum() {
        return resultEnum;
    }
}
