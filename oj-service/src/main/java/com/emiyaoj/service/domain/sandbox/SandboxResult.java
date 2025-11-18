package com.emiyaoj.service.domain.sandbox;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SandboxResult {
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
