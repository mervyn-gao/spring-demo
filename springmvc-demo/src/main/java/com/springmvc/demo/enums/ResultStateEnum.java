package com.springmvc.demo.enums;

/**
 * Created by mengran.gao on 2017/8/7.
 */
public enum ResultStateEnum {

    SUCCESS("200", "请求成功"),
    INNER_ERROR("500", "服务器内部异常");

    private String code;

    private String message;

    ResultStateEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
