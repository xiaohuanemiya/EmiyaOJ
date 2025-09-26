package com.emiyaoj.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@UtilityClass
public class Permissions {
    public static final int USER_MENU = 0x00000001;
    public static final int USER_LIST = 0x00000002;
    public static final int USER_ADD = 0x00000004;
    public static final int USER_EDIT = 0x00000008;
    public static final int USER_DELETE = 0x00000010;
    
    private static final String[] permissionArray = {
            "USER.MENU",
            "USER.LIST",
            "USER.ADD",
            "USER.EDIT",
            "USER.DELETE",
    };
    private static final int permissionCount = permissionArray.length;
    
    public String parsePermission(int code) {
        for (int i = 0; i < permissionCount; i++, code = code >> 1) {
            if ((code & 1) != 0) {
                return permissionArray[i];
            }
        }
        log.warn("查找的权限不存在！");
        return null;
    }
    
    public List<String> parsePermissions(int code) {
        List<String> permissions = new ArrayList<>(8);
        for (int i = 0; i < permissionCount; i++, code = code >> 1) {
            if ((code & 1) != 0) {
                permissions.add(permissionArray[i]);
            }
        }
        return permissions;
    }
    
    public Collection<? extends GrantedAuthority> parseGrantedAuthorities(int code) {
        Collection<GrantedAuthority> c = new ArrayList<>(8);
        for (int i = 0; i < permissionCount; i++, code = code >> 1) {
            if ((code & 1) != 0) {
                c.add(new SimpleGrantedAuthority(permissionArray[i]));
            }
        }
        return c;
    }
}
