package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.IUserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * ClassName UserServiceImpl
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/4 15:09
 * @version 1.0
 **/
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserDOMapper userMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userMapper获取到对应的用户user
        UserDO user = userMapper.selectByPrimaryKey(id);
        if(user == null){
            return null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPassword = userPasswordMapper.selectByUserId(id);
        return convertFromDomain(user, userPassword);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getName())
//            || userModel.getGender() == null
//            || userModel.getAge() == null
//            || StringUtils.isEmpty(userModel.getTelephone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        //实现model->dataObject即dao层映射的对象
        UserDO user = convertFromModel(userModel);
        try{
            userMapper.insertSelective(user);
        }
        catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "该手机号已经注册");
        }


        //这一步很关键，以及对应的UserDOMapper的insertSelective应该加上keyProperty属性以及设置主键是自增长的
        userModel.setId(user.getId());

        UserPasswordDO userPassword = convertPasswordFromModel(userModel);
        userPasswordMapper.insertSelective(userPassword);
        return;
    }

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        //通过用户手机号获取用户信息
        UserDO userDO = userMapper.selectByTelephone(telephone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDomain(userDO, userPasswordDO);
        //比对数据库中的密码以及用户输入的密码
        if(!StringUtils.equals(encrptPassword, userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    public UserModel getUserModelByIdInCache(Integer id) {
        UserModel userModel = (UserModel)redisTemplate.opsForValue().get("user_validate_" + id);
        if(userModel == null){
            userModel = this.getUserById(id);
            redisTemplate.opsForValue().set("user_validate_" + id, userModel);
            redisTemplate.expire("user_validate_" + id, 10, TimeUnit.MINUTES);
        }
        return userModel;
    }

    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO user = new UserDO();
        BeanUtils.copyProperties(userModel, user);
        return user;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPassword = new UserPasswordDO();
        userPassword.setUserId(userModel.getId());
        userPassword.setEncrptPassword(userModel.getEncrptPassword());
        return userPassword;
    }

    /**
     * 将User对象与UserPassword对象进行一个组合
     * @param user
     * @param userPassword
     * @return
     */
    private UserModel convertFromDomain(UserDO user, UserPasswordDO userPassword){
        if(user == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        if(userPassword != null){
            userModel.setEncrptPassword(userPassword.getEncrptPassword());
        }
        return userModel;
    }
}
