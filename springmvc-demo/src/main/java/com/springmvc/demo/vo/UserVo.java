package com.springmvc.demo.vo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Created by mengran.gao on 2017/8/7.
 */
public class UserVo {

    private Integer id;

//    @NotNull(message = "姓名不能为空", groups = {UserAddValidGroup.class})
//    @Length(min = 2, max = 20, message = "姓名长度必须在2——20之间", groups = UserAddValidGroup.class)
//    @Size(min = 2, max = 10, message = "姓名长度必须在2——20之间")
//    @Pattern(regexp = "\\d+", message = "姓名只能输入数字", groups = UserAddValidGroup.class)
    @Pattern(regexp = "1\\d{10}", message = "姓名只能输入数字", groups = UserAddValidGroup.class)
    private String name;

//    @NotNull(message = "年龄不能为空", groups = {UserAddValidGroup.class})
//    @Max(value = 200, message = "年龄不能大于200", groups = {UserAddValidGroup.class})
    private Integer age;

    @NotBlank(message = "邮箱不能为空")
//    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,4}", message = "邮件格式错误")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\\\d{8}", message = "手机号格式不正确")
    private String tel;

    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @NotNull(message = "出生日期不能为空")
    @Pattern(regexp = "yyyy-MM-dd", message = "出生日期格式不能")
    private Date birthDay;

    // 嵌套验证
//    @Valid
//    private Product product;

    private Set<String> tags;

    public UserVo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
