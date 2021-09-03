package com.miaoshaproject.controller;

/**
 * ClassName BaseController
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/4 20:42
 * @version 1.0
 **/

public class BaseController {
    //指定提交处理请求的提交内容类型
    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";
    /**
     * 定义exceptionhandle解决未被controller层吸收的exception
     * @param request
     * @param ex
     * @return
     */
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Object handlerException(HttpServletRequest request, Exception ex){
//        Map<String, Object> responseData = new HashMap<>();
//        if(ex instanceof BusinessException){
//            CommonReturnType commonReturnType = new CommonReturnType();
//            BusinessException businessException = (BusinessException)ex;
//            responseData.put("errCode", businessException.getErrCode());
//            responseData.put("errMsg", businessException.getErrMsg());
//        }
//        else{
//            responseData.put("errCode", EmBusinessError.UNKONW_ERROR.getErrCode());
//            responseData.put("errMsg", EmBusinessError.UNKONW_ERROR.getErrMsg());
//        }
//        return CommonReturnType.create(responseData, "fail");
//    }
}
