package com.springmvc.demo;

import com.springmvc.demo.test.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by mengran.gao on 2017/8/8.
 */
public class Application {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-*.xml");
        context.start();

        Test bean = context.getBean(Test.class);
        System.out.println(bean.getName());
        System.in.read();
    }
}
