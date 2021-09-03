package com.miaoshaproject.controller.viewObject;

import java.math.BigDecimal;

/**
 * ClassName ItemVO
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/6 16:53
 * @version 1.0
 **/

public class ItemVO {
    private Integer id;
    //商品的名称
    private String title;
    //商品的价格
    private BigDecimal price;
    //商品的库存
    private Integer stock;
    //商品的描述
    private String description;
    //商品的销量
    private Integer sales;
    //商品描述图片的url
    private String imgUrl;
    //记录商品是否在秒杀活动中
    private Integer promoStatus;
    //商品的秒杀价格
    private BigDecimal promoPrice;
    private Integer promoId;
    //秒杀活动开始时间，用来做倒计时展示用
    private String startDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
