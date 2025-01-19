package com.bin.binapibackend.common;

import java.io.Serializable;

/**
 * 错误码
 */
public enum ErrorCode implements Serializable {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "参数错误", ""),
    NULL_ERROR(40001, "空指针错误", ""),
    NOT_FOUND_ERROR(40002, "空指针错误", ""),
    NO_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    NO_test(4021, "42", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    OPERATION_ERROR(50001, "操作失败", "");


    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

}
