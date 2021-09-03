package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.PromoModel;

public interface IPromoService {
    /**
     * 根据itemId获取即将进行的或者正在进行的秒杀活动
     * @param itemId
     * @return
     */
    PromoModel getPromoByItemId(Integer itemId);

    /**
     * 发布活动
     * @param promoId
     */
    void publishPromo(Integer promoId);

    /**
     * 生成秒杀令牌
     * @param promoId
     * @return
     */
    String generateSecondKillToken(Integer promoId, Integer itemId, Integer userId) throws BusinessException;
}
