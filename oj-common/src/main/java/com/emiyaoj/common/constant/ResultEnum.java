package com.emiyaoj.common.constant;

public enum ResultEnum {
    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "错误请求"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "未找到"),
    METHOD_NOT_ALLOWED(405, "方法不被允许"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "冲突"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    NOT_IMPLEMENTED(501, "未实现"),
    BAD_GATEWAY(502, "错误网关"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP版本不受支持"),

    // 用户相关 1000-1999
    USER_NOT_FOUND(1000, "用户未找到"),
    USER_ALREADY_EXISTS(1001, "用户已存在"),
    INVALID_CREDENTIALS(1002, "无效的凭据"),
    ACCOUNT_LOCKED(1003, "账户已锁定"),
    PASSWORD_INCORRECT(1004, "密码错误"),
    BUSINESS_ERROR(8000, "业务错误"), USER_NAME_HAS_EXISTED(8001, "用户名已存在"),

    // 题目相关 2000-2999
    PROBLEM_NOT_FOUND(2000, "题目未找到"),
    PROBLEM_ALREADY_EXISTS(2001, "题目已存在"),

    // 提交相关 3000-3999
    SUBMISSION_NOT_FOUND(3000, "提交未找到"),
    SUBMISSION_FAILED(3001, "提交失败"),

    // 系统相关 4000-4999
    SYSTEM_ERROR(4000, "系统错误"),
    DATABASE_ERROR(4001, "数据库错误"),
    CACHE_ERROR(4002, "缓存错误"),

    // 文件相关 5000-5999
    FILE_NOT_FOUND(5000, "文件未找到"),
    FILE_UPLOAD_FAILED(5001, "文件上传失败"),

    // 权限相关 6000-6999
    PERMISSION_DENIED(6000, "权限被拒绝"),
    ROLE_NOT_FOUND(6001, "角色未找到"),

    // 第三方服务相关 7000-7999
    THIRD_PARTY_SERVICE_ERROR(7000, "第三方服务错误"),;

    // 自定义业务相关 8000-8999

    ResultEnum(int i, String msg) {

    }

    public String message() {
        return "";
    }
}
