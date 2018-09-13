package com.springmvc.demo.test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mengran.gao on 2018/5/17.
 */
@Component
public class Test {

    private String name = "yiyi";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 16, 10, 0);
        System.out.println(dateTime);
        long l = dateTime.toInstant(ZoneOffset.of("+0")).toEpochMilli();
        System.out.println(l);

        String a = "{\"msgId\":\"500287257261901824\",\"from\":\"1_9934493a-0019-41dd-bd1e-60adce50453f\",\"to\":\"2_518741b1-5873-455f-9e99-4f5d1b260704\",\"type\":\"txt\",\"timestamp\":1534047004130,\"chat_type\":\"chat\",\"msg\":\"退款怎么还没到账啊？\"}";
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(a, JsonObject.class);
        String msg = jsonObject.get("msg").getAsString();
        System.out.println(msg);

        System.out.println(System.currentTimeMillis());
        t();
    }

    public static void t() {
        List<String> la = Arrays.asList("aaa", "bbb");
        List<String> lb = Arrays.asList("ddd", "eee", "fff");
        List<String> lc = Arrays.asList("ggg", "hhh", "iii");
        la.forEach(a -> {
            if ("bbb".equals(a)) {
                return;
            }
            System.out.println("==" + a);
            lb.forEach(b -> {
                if ("eee".equals(b)) {
                    return;
                }
                System.out.println("====" + b);
                lc.forEach(c -> {
                    if ("hhh".equals(c)) {
                        return;
                    }
                    System.out.println("======" + c);
                });
            });
        });
    }
}
