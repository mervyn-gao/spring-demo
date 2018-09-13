package com.springmvc.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by mengran.gao on 2018/6/30.
 */
@RequestMapping("/users")
@Controller
public class UserPassController {

    @RequestMapping("/login")
    public String login(@ModelAttribute("username") String username, String password, Model model) {
        if ("mervyn".equals(username) && "123456".equals(password)) {
            return "users/main";
        }
        model.addAttribute("msg", "用户名密码错误");
        return "common/error";
    }
}