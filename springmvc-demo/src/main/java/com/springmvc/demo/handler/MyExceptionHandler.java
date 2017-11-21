package com.springmvc.demo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyExceptionHandler implements HandlerExceptionResolver {

    private static Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        logger.error("全局异常处理，异常信息：", exceptionStackTrace(ex));
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        Map<String, Object> model = new HashMap();
        model.put("code", 500);
        model.put("message", ex.getMessage());
        return new ModelAndView(view, model);
    }

    public static String exceptionStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTraceMsg = sw.toString();
        return stackTraceMsg;
    }
}
