package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.PromoDOMapper;
import com.miaoshaproject.dataobject.PromoDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.IItemService;
import com.miaoshaproject.service.IPromoService;
import com.miaoshaproject.service.IUserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.service.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ClassName PromoServiceImpl
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/7 15:46
 * @version 1.0
 **/
@Service
public class PromoServiceImpl implements IPromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;

    @Autowired
    private IItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IUserService userService;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀活动信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        //promoDO -> model
        PromoModel promoModel = convertFromPromoDO(promoDO);
        if(promoModel == null){
            return null;
        }
        //判断当前时间是否秒杀活动即将开始或者正在进行
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }
        else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }
        else{
            promoModel.setStatus(2);
        }


        return promoModel;
    }

    @Override
    public void publishPromo(Integer promoId) {
        //通过活动id获取活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if(promoDO.getItemId() == null || promoDO.getItemId() == 0){
            return;
        }
        ItemModel itemModel = itemService.getItemById(promoDO.getItemId());

        //将库存同步到redis内
        redisTemplate.opsForValue().set("promo_item_stock_" + itemModel.getId(), itemModel.getStock());

        //将秒杀大闸的限制数字设到redis内
        redisTemplate.opsForValue().set("promo_door_count_" + promoId, itemModel.getStock() * 5);

    }

    @Override
    public String generateSecondKillToken(Integer promoId, Integer itemId, Integer userId) throws BusinessException {
        //判断库存是否已售罄，若对应的售罄key存在，则直接返回下单失败
        if(redisTemplate.hasKey("promo_item_stock_invalid_" + itemId)){
            return null;
        }
        //获取对应商品的秒杀活动信息
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        //promoDO -> model
        PromoModel promoModel = convertFromPromoDO(promoDO);
        if(promoModel == null){
            return null;
        }
        //判断当前时间是否是秒杀活动即将开始或者正在进行
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }
        else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }
        else{
            promoModel.setStatus(2);
        }

        //判断活动是否正在进行
        if(promoModel.getStatus() != 2){
            return null;
        }
        //判断item信息是否存在
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);
        if(itemModel == null){
            return null;
        }
        //判断用户信息是否存在
        UserModel userModel = userService.getUserModelByIdInCache(userId);
        if (userModel == null){
            return null;
        }

        //获取秒杀大闸的count数量
        Long result = redisTemplate.opsForValue().increment("promo_door_count_" + promoId, -1);

        if(result < 0){
            return null;
        }

        //生成令牌并且存入redis内并给一个5分钟的有效期
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("promo_token_" + promoId + "_userId_" + userId + "_itemId_" + itemId, token);
        redisTemplate.expire("promo_token_" + promoId + "_userId_" + userId + "_itemId_" + itemId, 5, TimeUnit.MINUTES);
        return token;
    }

    private PromoModel convertFromPromoDO(PromoDO promoDO){
        if(promoDO == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
