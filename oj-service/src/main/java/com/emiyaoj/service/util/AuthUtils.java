package com.emiyaoj.service.util;

import com.emiyaoj.service.domain.pojo.User;
import com.emiyaoj.service.domain.pojo.UserLogin;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <h1>验证相关工具类</h1>
 * 尽可能避免使用Bean
 *
 * @author Erida
 * @since 2025/11/18
 */
@Slf4j
@UtilityClass
public class AuthUtils {
    private static boolean isTestEnvironment = false;
    
    private static UserLogin testUserLogin;
    
    public void setTestEnable(UserLogin ul) {
        testUserLogin = ul;  // 待扩展
        isTestEnvironment = true;
    }
    
    public void setTestDisable() {
        isTestEnvironment = false;
    }
    
    // 顶层方法，包括异常和日志处理
    public UserLogin getUserLogin() {
        if (isTestEnvironment) return testUserLogin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (UserLogin) authentication.getPrincipal();
        } catch (NullPointerException e) {
            log.warn("用户未登录: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.warn("未知错误: {}", e.getMessage());
            return null;
        }
    }
    
    // 顶层方法，包括异常和日志处理
    public User getUser() {
        UserLogin userLogin = getUserLogin();
        if (userLogin == null) return null;
        
        User user = userLogin.getUser();
        if (user != null) {
            return user;
        } else {
            log.warn("UserLogin存在但User为空! (UserLogin={})", userLogin);
            return null;
        }
    }
    
    public Long getUserId() {
        return getFromUser(User::getId);
    }
    
    /**
     * <p>满足任一角色</p>
     * 不考虑UserLogin存在但User不存在的情况
     */
    public boolean checkAnyRole(String... roles) {
        if (roles == null) return true;
        UserLogin userLogin = getUserLogin();
        if (userLogin == null) return false;
        return userLogin.getRoles().stream().anyMatch(List.of(roles)::contains);
    }
    
    /**
     * 满足所有角色（一般用不到吧）
     */
    public boolean checkAllRoles(String... roles) {
        if (roles == null) return true;
        UserLogin userLogin = getUserLogin();
        if (userLogin == null) return false;
        return Set.of(roles).containsAll(userLogin.getRoles());
    }
    
    /**
     * 满足任一权限
     */
    public boolean checkAnyPermission(String... permissions) {
        if (permissions == null) return true;
        UserLogin userLogin = getUserLogin();
        if (userLogin == null) return false;
        return userLogin.getPermissions().stream().anyMatch(List.of(permissions)::contains);
    }
    
    /**
     * 满足所有权限
     */
    public boolean checkAllPermissions(String... permissions) {
        if (permissions == null) return true;
        UserLogin userLogin = getUserLogin();
        if (userLogin == null) return false;
        return Set.of(permissions).containsAll(userLogin.getPermissions());
    }
    
    /**
     * 用户自定义可选条件
     */
    public boolean checkUser(Predicate<User> predicate) {
        User user = getUser();
        if (user == null) {
            return false;
        } else if (predicate == null) {
            return true;
        }
        return predicate.test(user);
    }
    
    public boolean checkUserLogin(Predicate<UserLogin> predicate) {
        UserLogin userLogin = getUserLogin();
        if (userLogin == null) return false;
        else if (predicate == null) return true;
        return predicate.test(userLogin);
    }
    
    /**
     * 优先检查角色，若通过则返回true。否则检查自定义条件（下同）
     */
    public boolean checkAnyRoleThenUserLogin(Predicate<UserLogin> predicate, String... roles) {
        UserLogin userLogin = getUserLogin();
        boolean accessRole = userLogin.getRoles().stream().anyMatch(List.of(roles)::contains);
        return accessRole || predicate.test(userLogin);
    }
    
    public boolean checkAllRolesThenUserLogin(Predicate<UserLogin> predicate, String... roles) {
        UserLogin userLogin = getUserLogin();
        boolean accessRole = Set.of(roles).containsAll(userLogin.getRoles());
        return accessRole || predicate.test(userLogin);
    }
    
    public boolean checkAnyPermissionThenUserLogin(Predicate<UserLogin> predicate, String... permissions) {
        UserLogin userLogin = getUserLogin();
        boolean accessPermission = userLogin.getPermissions().stream().anyMatch(List.of(permissions)::contains);
        return accessPermission || predicate.test(userLogin);
    }
    
    public boolean checkAllPermissionsThenUserLogin(Predicate<UserLogin> predicate, String... permissions) {
        UserLogin userLogin = getUserLogin();
        boolean accessPermission = Set.of(permissions).containsAll(userLogin.getPermissions());
        return accessPermission || predicate.test(userLogin);
    }
    
    public <T> T getFromUserLogin(Function<UserLogin, T> func) {
        UserLogin userLogin = getUserLogin();
        if (userLogin == null) return null;
        return func.apply(userLogin);
    }
    
    /**
     * 若只从User中获取某一属性，则可调用此方法更简单。
     * 若只获取UserId，则调用getUserId()
     * <h2>几个简单的例子</h2>
     * <ul>
     *     <li><code>getFromUser(User::getUsername)</code></li>
     *     <li><code>getFromUser(User::getNickname)</code></li>
     *     <li><code>getFromUser(User::getDeleted)</code></li>
     * </ul>
     */
    public <T> T getFromUser(Function<User, T> func) {
        User user = getUser();
        if (user == null || func == null) return null;
        return func.apply(user);
    }
    
    // 需要找一个方法实现读取本地文件静态常量值的方法来修改这个方法的返回值
    @Deprecated
    public boolean isTestEnvironment() {
        return isTestEnvironment;
    }
}
