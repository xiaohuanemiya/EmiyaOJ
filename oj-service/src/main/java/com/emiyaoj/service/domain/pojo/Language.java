package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 编程语言实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("language")
public class Language {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 语言名称，如 C, C++, Java, Python
     */
    private String name;
    
    /**
     * 版本，如 gcc-11, jdk-21, python-3.11
     */
    private String version;
    
    /**
     * 编译命令模板
     */
    private String compileCommand;
    
    /**
     * 执行命令模板
     */
    private String executeCommand;
    
    /**
     * 源文件扩展名，如 .c, .cpp, .java, .py
     */
    private String sourceFileExt;
    
    /**
     * 可执行文件扩展名
     */
    private String executableExt;
    
    /**
     * 是否需要编译：0-否，1-是
     */
    private Integer isCompiled;
    
    /**
     * 时间限制倍数
     */
    private BigDecimal timeLimitMultiplier;
    
    /**
     * 内存限制倍数
     */
    private BigDecimal memoryLimitMultiplier;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
