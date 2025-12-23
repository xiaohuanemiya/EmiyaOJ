package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 编程语言表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("language")
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 语言ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 语言名称，如 C, C++, Java, Python
     */
    @TableField("name")
    private String name;

    /**
     * 版本，如 gcc-11, jdk-21, python-3.11
     */
    @TableField("version")
    private String version;

    /**
     * 编译命令模板
     */
    @TableField("compile_command")
    private String compileCommand;

    /**
     * 执行命令模板
     */
    @TableField("execute_command")
    private String executeCommand;

    /**
     * 源文件扩展名，如 .c, .cpp, .java, .py
     */
    @TableField("source_file_ext")
    private String sourceFileExt;

    /**
     * 可执行文件扩展名
     */
    @TableField("executable_ext")
    private String executableExt;

    /**
     * 是否需要编译：0-否，1-是
     */
    @TableField("is_compiled")
    private Integer isCompiled;

    /**
     * 时间限制倍数
     */
    @TableField("time_limit_multiplier")
    private BigDecimal timeLimitMultiplier;

    /**
     * 内存限制倍数
     */
    @TableField("memory_limit_multiplier")
    private BigDecimal memoryLimitMultiplier;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
