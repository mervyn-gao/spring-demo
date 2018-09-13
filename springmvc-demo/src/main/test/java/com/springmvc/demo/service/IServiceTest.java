package com.springmvc.demo.service;

import com.springmvc.demo.service.impl.CityServiceImpl;
import org.junit.Test;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mengran.gao on 2018/6/27.
 */
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/spring-context.xml"
})*/
public class IServiceTest {

    @Resource
    private CityService cityService;
    @Resource
    private IService service;

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Test
    public void test2() throws Exception {
//        cityService.batchInsert(CityServiceImpl.getCityList("aa", 2));
        cityService.test();
        executorService.execute(() -> {
            service.batchInsert(CityServiceImpl.getCityList("bb", 1));
        });
//        cityService.test2();
//        System.in.read();
    }

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().withNano(0);
        LocalDateTime dateTime = LocalDateTime.now().withNano(0);

        System.out.println(date);
        System.out.println(time);
        System.out.println(dateTime);
    }

}