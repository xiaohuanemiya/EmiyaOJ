package com.emiyaoj.service.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;

@Data
@Builder
public class UserLoginVO {

    @Serial
    private static final long serialVersionUID = 4393557997355879737L;

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "令牌")
    private String token;

}
