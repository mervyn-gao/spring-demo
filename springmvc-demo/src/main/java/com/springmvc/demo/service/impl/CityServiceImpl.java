package com.springmvc.demo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springmvc.demo.mapper.CityMapper;
import com.springmvc.demo.model.City;
import com.springmvc.demo.model.CityExample;
import com.springmvc.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mengran.gao on 2017/8/25.
 */
@Service("cityService")
public class CityServiceImpl implements CityService {

    @Autowired private CityMapper cityMapper;

    @Override
    public City get(int id) {
        return cityMapper.selectById(id);
    }

    @Override
    public PageInfo<City> page(int page, int size) {
        PageHelper.offsetPage(page, size);
        CityExample example = new CityExample();
        List<City> cities = cityMapper.selectByExample(example);
        return new PageInfo<>(cities);
    }

    @Override
    public List<City> listByName(String name) {
        return cityMapper.listByName(name);
    }
}
