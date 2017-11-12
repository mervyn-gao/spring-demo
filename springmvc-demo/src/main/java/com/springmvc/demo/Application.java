package com.springmvc.demo;

import com.springmvc.demo.service.CityService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mengran.gao on 2017/8/8.
 */
public class Application {

    public static void main(String[] args) throws IOException {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-*.xml");
//        context.start();
//
//        System.in.read();

//        Method[] methods = CityService.class.getMethods();
//        for (Method method : methods) {
//            AnnotatedType annotatedReturnType = method.getAnnotatedReturnType();
//            String typeName = annotatedReturnType.getType().getTypeName();
//            System.out.println(typeName);
//
//            int modifiers = method.getModifiers();
//            System.out.println(modifiers);
//        }

//        String p = "^\\d+$";
//        Pattern pattern = Pattern.compile(p);
//        Matcher matcher = pattern.matcher("11122");
//        System.out.println(matcher.matches());
//
//        List<String> threeMarketId = new ArrayList<>(3);
//        threeMarketId.add("111");
//        threeMarketId.add("222");
//        threeMarketId.add("333");
//        System.out.println(threeMarketId.contains(111 + ""));

        Integer a = 1000;
        Integer b = 1000;
        System.out.println(a == b);
    }
}
