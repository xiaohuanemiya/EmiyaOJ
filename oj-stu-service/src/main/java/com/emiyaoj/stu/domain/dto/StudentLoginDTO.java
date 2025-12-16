package com.emiyaoj.stu.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 学生登录DTO
 */
@Data
public class StudentLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "账号不能为空")
    @Pattern(regexp = "^\\w{5,20}$", message = "用户名的长度必须为5~20位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\w{5,16}$", message = "密码的长度必须为5~16位")
    private String password;
}

