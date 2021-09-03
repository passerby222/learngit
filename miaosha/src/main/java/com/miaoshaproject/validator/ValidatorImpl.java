package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * ClassName ValidatorImpl
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/6 14:40
 * @version 1.0
 **/
@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;
    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        ValidationResult result = new ValidationResult();
        //如果传入的bean中的参数规则违背了对应validation定义的annotation
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if(constraintViolationSet.size() > 0){
            //有错误
            result.setHasErrors(true);
            for (ConstraintViolation<Object> objectConstraintViolation : constraintViolationSet) {
                    String errMsg = objectConstraintViolation.getMessage();
                    String propertyName = objectConstraintViolation.getPropertyPath().toString();
                    result.getErrorMsgMap().put(propertyName, errMsg);
            }
        }
        return result;
    }
}
