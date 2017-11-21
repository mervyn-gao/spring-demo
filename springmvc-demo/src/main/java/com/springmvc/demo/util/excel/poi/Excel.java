package com.springmvc.demo.util.excel.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mengran.gao on 2017/11/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Excel {

    // 列名
    String name() default "";

    // 单元格宽度
    int width() default 15;

    // 字段排序
    int sort() default 0;

    //日期格式化
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    //浮点数的精度
    int precision() default 1;
}
