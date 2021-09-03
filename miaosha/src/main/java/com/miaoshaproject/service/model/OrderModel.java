package com.miaoshaproject.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ClassName OrderModel
 * Description TODO
 * 用户下单的交易模型
 * @author 13299
 * Date 2020/8/7 9:47
 * @version 1.0
 **/

public class OrderModel implements Serializable {
    //交易号是有特殊含义的
    private String id;
    private Integer userId;
    private Integer itemId;
    //若非空，则表示以秒杀商品方式下单
    private Integer promoId;
    //若promoId非空，则表示秒杀商品价格
    private BigDecimal itemPrice;
    private Integer amount;
    //若promoId非空，则表示秒杀商品订单价格
    private BigDecimal orderPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
