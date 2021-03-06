package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.ItemStockDO;
import org.apache.ibatis.annotations.Param;

public interface ItemStockDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Thu Aug 06 15:41:44 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Thu Aug 06 15:41:44 CST 2020
     */
    int insert(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Thu Aug 06 15:41:44 CST 2020
     */
    int insertSelective(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Thu Aug 06 15:41:44 CST 2020
     */
    ItemStockDO selectByPrimaryKey(Integer id);

    /**
     * 通过商品id获得ItemStockDO
     * @param itemId
     * @return
     */
    ItemStockDO selectByItemId(Integer itemId);


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Thu Aug 06 15:41:44 CST 2020
     */
    int updateByPrimaryKeySelective(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Thu Aug 06 15:41:44 CST 2020
     */
    int updateByPrimaryKey(ItemStockDO record);

    /**
     * 减库存
     * @param itemId
     * @param amount
     * @return
     */
    int decreaseStock(@Param("itemId")Integer itemId, @Param("amount")Integer amount);
}