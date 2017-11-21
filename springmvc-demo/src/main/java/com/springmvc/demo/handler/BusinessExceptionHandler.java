package com.springmvc.demo.handler;

import com.springmvc.demo.exception.BusinessException;
import com.springmvc.demo.util.JsonUtils;
import com.springmvc.demo.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Created by mengran.gao on 2017/11/12.
 */
@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    // 在controller里面内容执行之前，校验一些参数不匹配，Get post方法不对之类
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //异常写入日志
        LOGGER.error(log(e));
        //打印方法入参
        LOGGER.info("方法入参：", JsonUtils.toJsonString(request.getParameterMap()));
        return new ResponseEntity<>(Result.failure(status.value(), status.getReasonPhrase()), status);
    }

    /**
     * 异常处理：统一抛出serviceException,在serviceException中根据CodeEnum区分异常，返回前端
     *
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<String> jsonHandler(HttpServletRequest request, Exception e) throws Exception {
        //异常写入日志
        LOGGER.error(log(e));
        if (e instanceof BusinessException) {
            BusinessException se = (BusinessException) e;
            return Result.failure(se.getBusinessStatus());
        }
        return Result.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部异常");
    }

    private static String log(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}