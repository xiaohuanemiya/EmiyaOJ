// Cmd.java
package com.emiyaoj.service.domain.pojo.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cmd {
    // Getters and Setters
    private List<String> args;
    private List<String> env;
    private List<BaseFile> files;
    private Boolean tty;
    
    // 资源限制
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
    
    // 文件操作
    private Map<String, BaseFile> copyIn;
    private List<String> copyOut;
    private List<String> copyOutCached;
    private Long copyOutMax;
    private Boolean copyOutTruncate;

}