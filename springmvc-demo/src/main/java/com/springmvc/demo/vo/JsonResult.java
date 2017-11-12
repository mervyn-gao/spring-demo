package com.springmvc.demo.vo;

import com.springmvc.demo.enums.ResultStateEnum;

/**
 * Created by mengran.gao on 2017/8/7.
 */
public class JsonResult<T> {

    private String code;

    private String message;

    private T data;

    public JsonResult() {
    }

    public JsonResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> JsonResult<T> success(ResultStateEnum resultState, T data) {
        return new JsonResult<>(resultState.getCode(), resultState.getMessage(), data);
    }

    public static <T> JsonResult<T> fail(ResultStateEnum resultState) {
        return new JsonResult<>(resultState.getCode(), resultState.getMessage());
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
