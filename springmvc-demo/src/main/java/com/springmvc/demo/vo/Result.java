package com.springmvc.demo.vo;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by mengran.gao on 2017/7/7.
 */
public class Result<T> implements Serializable {

    private int code;

    private String message;

    private T data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(T data, int code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(HttpStatus.OK.value(), message);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(data, HttpStatus.OK.value(), message);
    }

    public static <T> Result<T> failure(BusinessStatus businessStatus) {
        return new Result<>(businessStatus.getCode(), businessStatus.getMessage());
    }

    public static <T> Result<T> failure(int code, String message) {
        return new Result<>(code, message);
    }

}
