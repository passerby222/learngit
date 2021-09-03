package com.miaoshaproject.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ClassName ItemModel
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/6 15:18
 * @version 1.0
 **/

public class ItemModel implements Serializable {
    private Integer id;
    //商品的名称
    @NotBlank(message = "商品名称不能为空")
    private String title;
    //商品的价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格必须大于0")
    private BigDecimal price;
    //商品的库存
    @NotNull(message = "库存必须填")
    private Integer stock;
    //商品的描述
    @NotBlank(message = "商品描述不能为空")
    private String description;
    //商品的销量
    private Integer sales;
    //商品描述图片的url
    @NotBlank(message = "图片信息不能为空")
    private String imgUrl;

    //使用聚合模型，如果promoModrl不为空，则表示其拥有还未结束的秒杀活动
    private PromoModel promoModel;

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

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }
}
