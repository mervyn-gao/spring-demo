package com.springmvc.demo.service;

import com.springmvc.demo.model.User;

import java.io.InputStream;

/**
 * Created by mengran.gao on 2017/11/7.
 */
public interface UserService {

    void importWorkers(InputStream inputStream);

    void edit(User user);
}
