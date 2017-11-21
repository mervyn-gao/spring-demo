package com.springmvc.demo.service;

import com.github.pagehelper.PageInfo;
import com.springmvc.demo.controller.CityController;
import com.springmvc.demo.mapper.CityMapper;
import com.springmvc.demo.model.City;
import org.hibernate.validator.constraints.Email;

import java.util.List;

/**
 * Created by mengran.gao on 2017/8/25.
 */
public interface CityService {

    @Email(groups = {CityMapper.class, CityController.class}, regexp = "aaa@aa.aa")
    City get(int id);

    PageInfo<City> page(int page, int size);

    List<City> listByName(String name);
}
