package com.emiyaoj.common.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Permissions {
    public static final int USER_LIST = 0x00000001;
    public static final int USER_ADD = 0x00000002;
    public static final int USER_EDIT = 0x00000004;
    public static final int USER_DELETE = 0x00000008;
    
    private static final String[] permissionArray = {
            "USER.LIST",
            "USER.ADD",
            "USER.EDIT",
            "USER.DELETE",
    };
    private static final int permissionCount = permissionArray.length;
    
    public List<String> parsePermissions(int code) {
        List<String> permissions = new ArrayList<>(8);
        for (int i = 0; i < permissionCount; i++, code = code >> 1) {
            if ((code & 1) != 0) {
                permissions.add(permissionArray[i]);
            }
        }
        return permissions;
    }
}
