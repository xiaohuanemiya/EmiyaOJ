// Status.java
package com.emiyaoj.service.domain.pojo.oj.Enum;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

import static com.alibaba.fastjson.parser.JSONToken.LITERAL_STRING;

@JSONType(deserializer = Status.StatusDeserializer.class)

public enum Status {
    Accepted("Accepted"),
    MemoryLimitExceeded("Memory Limit Exceeded"),
    TimeLimitExceeded("Time Limit Exceeded"),
    OutputLimitExceeded("Output Limit Exceeded"),
    FileError("File Error"),
    NonzeroExitStatus("Nonzero Exit Status"),
    Signalled("Signalled"),
    InternalError("Internal Error");


    private final String value;

    Status(String value) {
        this.value = value;
    }

    @JSONField
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JSONCreator
    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }

    // FastJSON 自定义反序列化器
    public static class StatusDeserializer implements ObjectDeserializer {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T deserialze(DefaultJSONParser parser,
                                Type type, Object fieldName) {
            String value = parser.parseObject(String.class);
            return (T) Status.fromValue(value);
        }

        @Override
        public int getFastMatchToken() {
            return LITERAL_STRING;
        }
    }

}