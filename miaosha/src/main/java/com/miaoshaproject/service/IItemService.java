package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

public interface IItemService {
    /**
     * 创建商品
     * @param itemModel
     * @return
     */
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    /**
     * 商品列表浏览
     * @return
     */
    List<ItemModel> listItem();

    /**
     * 商品详情浏览
     * @param id
     * @return
     */
    ItemModel getItemById(Integer id);

    /**
     * item及promo model缓存模型
     * @param id
     * @return
     */
    ItemModel getItemByIdInCache(Integer id);


    /**
     * 库存扣减
     * @param itemId
     * @param amount
     * @return
     */
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    /**
     * 库存回补
     * @param itemId
     * @param amount
     * @return
     */
    boolean increaseStock(Integer itemId, Integer amount) throws BusinessException;
    /**
     * 商品销量的增加
     * @param itemId
     * @param amount
     */

    void increaseSales(Integer itemId, Integer amount) throws BusinessException;

    /**
     * 异步更新库存
     * @param itemId
     * @param amount
     * @return
     */
    boolean asyncDecreaseStock(Integer itemId, Integer amount);

    /**
     * 初始化库存流水
     * @param itemId
     * @param amount
     */
    String initStockLog(Integer itemId, Integer amount);
}
