package com.springmvc.demo.service;

import com.github.pagehelper.PageInfo;
import com.springmvc.demo.model.City;

import java.util.List;

/**
 * Created by mengran.gao on 2017/8/25.
 */
public interface CityService {
    City get(Long id);

    PageInfo<City> page(int page, int size);

    void test();

    void batchInsert(List<City> cityList);
}
