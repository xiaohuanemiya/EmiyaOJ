package com.emiyaoj.service.domain.sandbox;

import lombok.Data;

@Data
public class FileError {
    private String name;
    private FileErrorType type;
    private String message;
}
