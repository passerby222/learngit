package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

public interface IOrderService {
    /**
     * 下订单
     * @param userId
     * @param itemId
     * @param amount
     * @return
     */
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount, String stockLogId) throws BusinessException;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               }
