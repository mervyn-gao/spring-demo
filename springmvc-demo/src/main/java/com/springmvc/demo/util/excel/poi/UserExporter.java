package com.springmvc.demo.util.excel.poi;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by mengran.gao on 2017/11/6.
 */
public class UserExporter extends BaseExporter {
    @Excel(name = "用户名", sort = 1)
    private String username;
    @Excel(name = "年龄", sort = 2)
    private int age;
    @Excel(name = "邮箱", sort = 3)
    private String email;
    @Excel(name = "出生日期", dateFormat = "yyyy-MM-dd", sort = 4)
    private Date birthday;
    @Excel(name = "薪水", precision = 2, sort = 5)
    private BigDecimal salary;

    public UserExporter() {
    }

    public UserExporter(String username, int age, String email, Date birthday, BigDecimal salary) {
        this.username = username;
        this.age = age;
        this.email = email;
        this.birthday = birthday;
        this.salary = salary;
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

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "UserExporter{" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", salary=" + salary +
                '}';
    }
}
