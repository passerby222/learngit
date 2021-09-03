package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

public interface IUserService {
    /**
     * 根据用户id获得用户对象
     * @param id
     */
    UserModel getUserById(Integer id);

    /**
     * 用户注册
     * @param userModel
     */
    void register(UserModel userModel) throws BusinessException;

    /**
     * 用户登录
     * @param telephone 用户注册手机
     * @param encrptPassword 用户加密后的密码
     */
    UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException;

    /**
     * 通过缓存获取用户对象
     * @param id
     * @return
     */
    UserModel getUserModelByIdInCache(Integer id);
}
