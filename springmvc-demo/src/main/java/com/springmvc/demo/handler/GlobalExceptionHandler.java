package com.springmvc.demo.handler;

import com.springmvc.demo.exception.BusinessException;
import com.springmvc.demo.util.SpringContextUtils;
import com.springmvc.demo.vo.base.Result;
import com.springmvc.demo.vo.base.ResultCode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * 统一异常处理
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 这里可以对springmvc封装的常见异常作一些处理，springmvc封装的时候是没有返回信息的，只有请求的状态及头信息，
     * 没有response body信息，即Object body为空
     */
    /*@Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "";
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            //不支持'{}'请求：支持{}请求, ex.getMethod(), ex.getSupportedMethods();
            //Request method '{}' not supported, supported {}
            message = SpringContextUtils.getApplicationContext()
                    .getMessage(status.value() + "", null, status.getReasonPhrase(), LocaleContextHolder.getLocale());
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            //不支持'{}'请求类型, ex.getContentType()
            //Content type '{}' not supported, ex.getContentType()
            System.out.println(ex);
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            System.out.println(ex);
        } else if (ex instanceof MissingPathVariableException) {
            System.out.println(ex);
        } else if (ex instanceof MissingServletRequestParameterException) {
            System.out.println(ex);
        } else if (ex instanceof ServletRequestBindingException) {
            System.out.println(ex);
        } else if (ex instanceof ConversionNotSupportedException) {
            System.out.println(ex);
        } else if (ex instanceof TypeMismatchException) {
            System.out.println(ex);
        } else if (ex instanceof HttpMessageNotReadableException) {
            System.out.println(ex);
        } else if (ex instanceof HttpMessageNotWritableException) {
            System.out.println(ex);
        } else if (ex instanceof MethodArgumentNotValidException) {
            System.out.println(ex);
        } else if (ex instanceof MissingServletRequestPartException) {
            System.out.println(ex);
        } else if (ex instanceof BindException) {
            System.out.println(ex);
        } else if (ex instanceof NoHandlerFoundException) {
            System.out.println(ex);
        } else if (ex instanceof AsyncRequestTimeoutException) {
            System.out.println(ex);
        } else {
            System.out.println(ex);
            if (logger.isWarnEnabled()) {
                logger.warn("Unknown exception type: " + ex.getClass().getName());
            }
        }
        return ResponseEntity.status(status).headers(headers).body(Result.failure(status.value(), message));
    }*/

    /**
     * 这个主要是统一处理Hibernate-validator的字段校验提示信息，这里统一处理就不用在Controller的方法中添加BindingResult result参数了
     * 这里也可以在重写handleExceptionInternal方法中else if (ex instanceof MethodArgumentNotValidException)这里处理
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        StringBuilder builder = new StringBuilder("[");
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> builder.append(error.getDefaultMessage()).append(","));
        }
        builder.replace(builder.length() - 1, builder.length(), "]");
        return ResponseEntity.status(status).headers(headers)
                .body(Result.failure(ResultCode.PARAM_IS_INVALID.getCode(), builder.toString()));
    }

    /* 处理自定义异常,status给200表示请求是成功的 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result> handleBusinessException(BusinessException e) {
        ResultCode resultCode = e.getResultCode();
        int code = resultCode.getCode();
        String message = SpringContextUtils.getApplicationContext()
                .getMessage(code + "", null, resultCode.getMessage(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.OK).body(Result.failure(code, message));
    }

    /* 处理运行时异常 */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        // 可通过邮件、微信公众号等方式发送信息至开发人员、记录存档等操作
        ResultCode resultCode = ResultCode.SYSTEM_INNER_ERROR;
        int code = resultCode.getCode();
        String message = SpringContextUtils.getApplicationContext()
                .getMessage(code + "", null, resultCode.getMessage(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.failure(code, message));
    }
}
