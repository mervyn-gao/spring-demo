package com.springmvc.demo.util.excel.poi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by mengran.gao on 2017/11/15.
 */
public class ReflectUtils2 {

    private static Logger LOGGER = LoggerFactory.getLogger(ReflectUtils2.class);

    public static Field[] getClassFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }

    public static Field[] getClassAndSuperClassFields(Class<?> clazz) {
        Field[] resultFields;
        Field[] clazzFields = clazz.getDeclaredFields();
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            Field[] superFields = superClass.getDeclaredFields();
            resultFields = new Field[clazzFields.length + superFields.length];
            System.arraycopy(superFields, 0, resultFields, clazzFields.length, superFields.length);
        } else {
            resultFields = new Field[clazzFields.length];
        }
        System.arraycopy(clazzFields, 0, resultFields, 0, clazzFields.length);
        return resultFields;
    }

    public static Object getValueByFieldName(Field field, Object o) {
        field.setAccessible(true);
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            LOGGER.error("{}段非法访问", field.getName());
            throw new ExcelException(field.getName() + "段非法访问");
        }
    }

    public static Object getValueByFieldName(String fieldName, Object o) {
        Field field = getFieldByName(fieldName, o.getClass());
        field.setAccessible(true);
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            LOGGER.error("{}段非法访问", fieldName);
            throw new ExcelException(fieldName + "段非法访问");
        }
    }

    public static Field getFieldByName(String fieldName, Class<?> clazz) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            LOGGER.error("字段解析出错，字段名称：{}", fieldName);
            throw new ExcelException("字段解析出错，字段名称：" + fieldName);
        }
        if (field != null) return field;
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return getFieldByName(fieldName, superClass);
        }
        return null;
    }

    public static void main(String[] args) {
        Field[] classFields = getClassFields(UserExporter.class);
        for (Field field : classFields) {
            System.out.println(field.getName());
        }
        System.out.println("=======================");
        Field[] classFields2 = getClassAndSuperClassFields(UserExporter.class);
        for (Field field : classFields2) {
            System.out.println(field.getName());
        }
        System.out.println("=======================");
    }
}
