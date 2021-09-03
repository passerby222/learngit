package com.miaoshaproject.response;

/**
 * ClassName CommonReturnType
 * Description TODO
 * 归一化ResponseBody返回的参数格式
 * @author 13299
 * Date 2020/8/4 16:33
 * @version 1.0
 **/

public class CommonReturnType {
    /**
     * status表明对应请求的返回处理结果 “success”或者“fail”
     */
    private String status;
    /**
     * 若status=success,则data内返回前端需要的json数据
     * 若status=fail,则data内使用通用的错误码格式
     */
    private Object data;

    //定义一个通用的创建方法  二重奏
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status){
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }




}
