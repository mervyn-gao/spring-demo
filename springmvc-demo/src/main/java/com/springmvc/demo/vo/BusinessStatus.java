package com.springmvc.demo.vo;

/**
 * 业务异常编码
 * code编码规则：2:服务级别错误 001:模块 01:具体错误
 */
public enum BusinessStatus {

    CHECK_ERROR(100101, "参数校验错误"),

    EX_1(101, "异常1"),
    EX_2(102, "异常2");

    private int code;
    private String message;

    BusinessStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
