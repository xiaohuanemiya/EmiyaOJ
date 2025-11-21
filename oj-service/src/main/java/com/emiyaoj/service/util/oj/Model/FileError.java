// FileError.java
package com.emiyaoj.service.util.oj.Model;

import com.emiyaoj.service.util.oj.Enum.FileErrorType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileError {
    // Getters and Setters
    private String name;
    private FileErrorType type;
    private String message;

    // Constructors
    public FileError() {}

    public FileError(String name, FileErrorType type) {
        this.name = name;
        this.type = type;
    }

    public FileError(String name, FileErrorType type, String message) {
        this.name = name;
        this.type = type;
        this.message = message;
    }

}