// Result.java
package com.emiyaoj.service.util.oj.Model;

import com.emiyaoj.service.util.oj.Enum.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    // Getters and Setters
    private Status status;
    private String error;
    private Integer exitStatus;
    private Long time;
    private Long memory;
    private Integer procPeak;
    private Long runTime;
    private Map<String, String> files;
    private Map<String, String> fileIds;
    private List<FileError> fileError;

}