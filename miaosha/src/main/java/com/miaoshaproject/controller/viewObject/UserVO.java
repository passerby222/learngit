package com.miaoshaproject.controller.viewObject;

/**
 * ClassName UserVO
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/4 16:18
 * @version 1.0
 **/

public class UserVO {
    private Integer id;
    private String name;
    private Integer gender;
    private Integer age;
    private String telephone;

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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
