package com.emiyaoj.service.domain.vo;

import lombok.Data;

@Data
public class LoginVO {
    // Getters and Setters
    private String token;
    private static String type = "Bearer";
    private String username;
    private Long userId;

    public LoginVO(String token, String username, Long userId) {
        this.token = type+" "+token;
        this.username = username;
        this.userId = userId;
    }

}
