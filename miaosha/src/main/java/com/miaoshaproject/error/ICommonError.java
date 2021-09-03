package com.miaoshaproject.error;

public interface ICommonError {
     int getErrCode();
     String getErrMsg();
     ICommonError setErrMsg(String errMsg);
}
