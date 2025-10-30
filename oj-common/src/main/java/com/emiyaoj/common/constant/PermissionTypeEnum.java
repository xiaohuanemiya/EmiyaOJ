package com.emiyaoj.common.constant;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PermissionTypeEnum {
    MENU(1, "菜单"),
    BUTTON(2, "按钮"),
    LINK(3, "接口");

    @EnumValue
    public final Integer value;
    public final String desc;


}
