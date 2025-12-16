package com.emiyaoj.stu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 学生登录返回VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String nickname;
    private String token;
}

