package com.springmvc.demo.controller;

import com.github.pagehelper.PageInfo;
import com.springmvc.demo.model.City;
import com.springmvc.demo.service.CityService;
import com.springmvc.demo.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mengran.gao on 2017/8/25.
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private CityService cityService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public City get(@PathVariable Integer id) {
        return cityService.get(id);
    }


    @RequestMapping(method = RequestMethod.POST, value = "listByName")
    public Result<List<City>> list() {
        List<City> cities = cityService.listByName("aaa");
        return Result.success(cities);
    }

    @RequestMapping(method = RequestMethod.POST, value = "page")
    public Result<PageInfo<City>> page() {
        PageInfo<City> pageInfo = cityService.page(1, 10);
        return Result.success(pageInfo);
    }
}
