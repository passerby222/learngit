package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewObject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.IUserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ClassName UserController
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/4 15:06
 * @version 1.0
 **/
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController{
    @Autowired
    private IUserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone")String telephone,
                                  @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //请求参数校验
        if(StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登陆服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telephone, this.encodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内

        //修改成若用户登录验证成功后，将对应的登录信息和登录凭证一起存入redis

        //生成登录凭证token, UUID
        String uuidToken = UUID.randomUUID().toString();
        uuidToken = uuidToken.replace("-","" );

        //建立token和用户登录态之间的联系
        redisTemplate.opsForValue().set(uuidToken, userModel);
        //设置超时时间
        redisTemplate.expire(uuidToken, 1, TimeUnit.HOURS);
//        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
//        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        //下发了token
        return CommonReturnType.create(uuidToken);
    }

    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telephone")String telephone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号要和对应的otpcode相符合
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
        //之所以使用druid类库里的equals，是因为他做了判空的处理
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }
        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setTelephone(telephone);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.encodeByMd5(password));
        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    public String encodeByMd5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        return base64en.encode(md5.digest(str.getBytes("utf-8")));
    }

    /**
     * 用户获取otp短信接口
     * RequestMapping标签下的consumes属性为指定处理请求的提交内容类型
     * @param telephone
     * @return
     */
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telephone")String telephone){
        //需要按照一定的规则生成otp验证码,就按照随机数生成方式
        Random random = new Random();
        //此时随机数取值为[0,99999)
        int randomInt = random.nextInt(99999);
        //此时随机数取值为[10000,109999)
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        //将otp验证码同对应用户的手机号关联, 使用httpsession的方式绑定他的手机号与otpcode
        httpServletRequest.getSession().setAttribute(telephone, otpCode);
        //将otp验证码通过短信通道发送给用户，省略
        System.out.println("telephone = " + telephone + " & otpCode = " + otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id")int id) throws BusinessException {
        //调用Service层方法获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //如果获取的对应用户信息不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型对象转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);

        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
}
