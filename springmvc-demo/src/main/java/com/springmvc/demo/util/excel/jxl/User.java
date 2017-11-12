package com.springmvc.demo.util.excel.jxl;

import java.util.Date;

/**
 * Created by mengran.gao on 2017/11/6.
 */
public class User {
    private String username;
    private int age;
    private String email;
    private Date birthday;

    public User(String username, int age, String email, Date birthday) {
        this.username = username;
        this.age = age;
        this.email = email;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
