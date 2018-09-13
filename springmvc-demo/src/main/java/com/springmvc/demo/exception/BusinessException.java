package com.springmvc.demo.exception;

import com.springmvc.demo.vo.base.ResultCode;

/**
 * Created by mengran.gao on 2017/7/7.
 */
public class BusinessException extends RuntimeException {

    private ResultCode resultCode;

    public BusinessException() {
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
