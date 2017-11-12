package com.springmvc.demo.controller;

import com.github.pagehelper.PageInfo;
import com.springmvc.demo.enums.ResultStateEnum;
import com.springmvc.demo.model.City;
import com.springmvc.demo.service.CityService;
import com.springmvc.demo.vo.JsonResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by mengran.gao on 2017/8/25.
 */
@RestController
@RequestMapping("/citys")
public class CityController {

    @Resource
    private CityService cityService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public City get(@PathVariable Integer id) {
        return cityService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "page")
    public JsonResult<PageInfo<City>> page() {
        PageInfo<City> pageInfo = cityService.page(1, 10);
        return JsonResult.success(ResultStateEnum.SUCCESS, pageInfo);
    }
}
