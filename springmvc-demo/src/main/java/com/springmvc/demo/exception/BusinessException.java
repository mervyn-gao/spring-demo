package com.springmvc.demo.exception;

import com.springmvc.demo.vo.BusinessStatus;

/**
 * Created by mengran.gao on 2017/7/7.
 */
public class BusinessException extends RuntimeException {

    private BusinessStatus businessStatus;

    public BusinessException() {
    }

    public BusinessException(BusinessStatus businessStatus) {
        super(businessStatus.getMessage());
        this.businessStatus = businessStatus;
    }

    public BusinessException(BusinessStatus businessStatus, Throwable cause) {
        super(businessStatus.getMessage(), cause);
        this.businessStatus = businessStatus;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessStatus getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(BusinessStatus businessStatus) {
        this.businessStatus = businessStatus;
    }
}
