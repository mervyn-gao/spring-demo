package com.springmvc.demo.service.impl;

import com.springmvc.demo.mapper.CityMapper;
import com.springmvc.demo.model.City;
import com.springmvc.demo.service.IService;
import com.springmvc.demo.util.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mengran.gao on 2018/6/27.
 */
@Service("ServiceImpl1")
public class ServiceImpl1 implements IService {

    @Autowired
    private CityMapper cityMapper;
    @Override
    public void test() {
        System.out.println("ServiceImpl1 aaaaaa");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<City> cityList) {
        JdbcUtil.executeInsertSelectiveBatch(cityMapper, cityList);
    }
}
