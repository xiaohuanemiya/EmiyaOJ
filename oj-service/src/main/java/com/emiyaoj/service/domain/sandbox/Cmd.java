package com.emiyaoj.service.domain.sandbox;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Cmd {
    private List<String> args;
    private List<String> env;
    private List<Object> files;
    private Boolean tty;
    
    // Resource limits
    private Long cpuLimit;
    private Long clockLimit;
    private Long memoryLimit;
    private Long stackLimit;
    private Integer procLimit;
    private Integer cpuRateLimit;
    private String cpuSetLimit;
    private Boolean strictMemoryLimit;
    private Boolean dataSegmentLimit;
    private Boolean addressSpaceLimit;
    
    // File operations
    private Map<String, Object> copyIn;
    private List<String> copyOut;
    private List<String> copyOutCached;
    private Long copyOutMax;
    private Boolean copyOutTruncate;
}
