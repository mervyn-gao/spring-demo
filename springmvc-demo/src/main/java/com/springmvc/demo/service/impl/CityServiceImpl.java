package com.springmvc.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springmvc.demo.mapper.CityMapper;
import com.springmvc.demo.model.City;
import com.springmvc.demo.model.CityExample;
import com.springmvc.demo.service.CityService;
import com.springmvc.demo.service.IService;
import com.springmvc.demo.util.JdbcUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mengran.gao on 2017/8/25.
 */
@Service("cityService")
public class CityServiceImpl implements CityService {

    @Autowired
    private IService service;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private DataSourceTransactionManager txManager;
    /**
     * 初始化线程池
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    public City get(Long id) {
        return cityMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<City> page(int page, int size) {
        PageHelper.offsetPage(page, size);
        CityExample example = new CityExample();
        List<City> cities = cityMapper.selectByExample(example);
        return new PageInfo<>(cities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<City> cityList) {
        JdbcUtil.executeInsertSelectiveBatch(cityMapper, cityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void test() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc");
        List<String> errList = new ArrayList<>();
        AtomicInteger ai = new AtomicInteger(0);
//        CityService proxyService = (CityServiceImpl) AopContext.currentProxy();
        list.forEach(str -> {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        batchInsert(getCityList(str, ai.addAndGet(1)));
                    } catch (Exception e) {
                        errList.add(str);
                    }
                }
            });
        });
        if (!errList.isEmpty()) {
            System.out.println("errList:" + errList);
        }
        City record = new City();
        record.setId(1L);
        record.setName("yiyi2");
        cityMapper.updateByPrimaryKeySelective(record);
    }

    public static List<City> getCityList(String name, int idx) {
        List<City> cityList = new ArrayList<>();
        City city = new City();
        city.setName(name + (idx * 2 - 1));
        city.setSex("1");
        city.setState("武汉");
        city.setSal(new BigDecimal(10000));
        cityList.add(city);
        city = new City();
        city.setName(name + (idx * 2));
        city.setSex(idx == 2 ? "00" : "0");
        city.setState("襄阳");
        city.setSal(new BigDecimal(5000));
        cityList.add(city);
        return cityList;
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc");
        List<String> errList = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(list.size());
        AtomicInteger ai = new AtomicInteger(0);
        list.forEach(str -> {
            executorService.execute(() -> {
                try {
                    if ("bbb".equals(str)) {
                        System.out.println(1 / 0);
                    } else {
                        System.out.println(str);
                    }
                } catch (Exception e) {
                    errList.add(str);
                } finally {
                    latch.countDown();
                }
            });
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!errList.isEmpty()) {
            System.out.println("errList:" + errList);
        }
        executorService.shutdown();
    }
}
