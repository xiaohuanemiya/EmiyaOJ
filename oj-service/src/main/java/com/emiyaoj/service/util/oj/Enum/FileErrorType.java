// FileErrorType.java
package com.emiyaoj.service.util.oj.Enum;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

@JSONType(deserializer = FileErrorType.FileErrorTypeDeserializer.class)
public enum FileErrorType {
    CopyInOpenFile("CopyInOpenFile"),
    CopyInCreateFile("CopyInCreateFile"),
    CopyInCopyContent("CopyInCopyContent"),
    CopyOutOpen("CopyOutOpen"),
    CopyOutNotRegularFile("CopyOutNotRegularFile"),
    CopyOutSizeExceeded("CopyOutSizeExceeded"),
    CopyOutCreateFile("CopyOutCreateFile"),
    CopyOutCopyContent("CopyOutCopyContent"),
    CollectSizeExceeded("CollectSizeExceeded");

    @JSONField
    private final String value;

    FileErrorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JSONCreator
    public static FileErrorType fromValue(String value) {
        for (FileErrorType type : FileErrorType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown file error type: " + value);
    }

    // FastJSON 自定义反序列化器
    public static class FileErrorTypeDeserializer implements com.alibaba.fastjson.parser.deserializer.ObjectDeserializer {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser parser,
                                java.lang.reflect.Type type, Object fieldName) {
            String value = parser.parseObject(String.class);
            return (T) FileErrorType.fromValue(value);
        }

        @Override
        public int getFastMatchToken() {
            return com.alibaba.fastjson.parser.JSONToken.LITERAL_STRING;
        }
    }
}