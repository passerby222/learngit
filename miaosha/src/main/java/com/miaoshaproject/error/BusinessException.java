package com.miaoshaproject.error;

/**
 * ClassName BusinessException
 * Description TODO
 * 包装器业务异常类实现
 * @author 13299
 * Date 2020/8/4 19:39
 * @version 1.0
 **/

public class BusinessException extends Exception implements ICommonError{
    private ICommonError commonError;

    /**
     * 直接接收EmBusinessError的传参用于构造业务异常
     * @param commonError
     */
    public BusinessException(ICommonError commonError){
        //Exception自身会有一些初始化的机制
        super();
        this.commonError = commonError;
    }

    public BusinessException(ICommonError commonError, String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public ICommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
