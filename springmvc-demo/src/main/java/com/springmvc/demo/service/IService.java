package com.springmvc.demo.service;

import com.springmvc.demo.model.City;

import java.util.List;

/**
 * Created by mengran.gao on 2018/6/27.
 */
public interface IService {

    void test();


    void batchInsert(List<City> cityList);
}
