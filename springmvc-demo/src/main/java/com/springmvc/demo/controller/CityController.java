package com.springmvc.demo.controller;

import com.github.pagehelper.PageInfo;
import com.springmvc.demo.config.JedisConfig;
import com.springmvc.demo.model.City;
import com.springmvc.demo.service.CityService;
import com.springmvc.demo.vo.base.Result;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengran.gao on 2017/8/25.
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private CityService cityService;

    @Autowired
    private JedisConfig jedisConfig;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public City get(@PathVariable Long id) {
        System.out.println(jedisConfig.getHost() + ":" + jedisConfig.getPort());
        return cityService.get(id);
    }


    @RequestMapping(method = RequestMethod.POST, value = "listByName")
    public Result<List<City>> list() {
        List<City> cities = new ArrayList<>();
        return Result.success(cities);
    }

    @RequestMapping(method = RequestMethod.POST, value = "page")
    public Result<PageInfo<City>> page() {
        PageInfo<City> pageInfo = cityService.page(1, 10);
        return Result.success(pageInfo);
    }

    @RequestMapping(value = "download")
    public ResponseEntity<byte[]> download(String filePath) throws IOException {
        URL url = new URL(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = URLEncoder.encode("店铺黑名单.xlsx", "UTF-8");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + "; filename*=utf-8''" + filename);
        return new ResponseEntity<>(IOUtils.toByteArray(url), headers, HttpStatus.OK);
    }
}
