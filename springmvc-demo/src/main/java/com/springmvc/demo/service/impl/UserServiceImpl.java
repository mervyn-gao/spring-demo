package com.springmvc.demo.service.impl;

import com.springmvc.demo.model.User;
import com.springmvc.demo.service.UserService;
import com.springmvc.demo.util.excel.poi.ExcelUtil;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mengran.gao on 2017/11/7.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Override
    public void importWorkers(InputStream in) {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("username", "用户名");
        fieldMap.put("age", "年龄");
        fieldMap.put("email", "邮箱");
        fieldMap.put("birthday", "出生日期");
        List<User> users = ExcelUtil.excelToList(in, 0, User.class, fieldMap);
        System.out.println(users);
    }
}
