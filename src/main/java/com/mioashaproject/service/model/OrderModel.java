package com.mioashaproject.service.model;

import java.math.BigDecimal;

/**
 * Created by Huzhimin on 2019/04/21/0021 13:31
 */
public class OrderModel {
    //订单号ID
    private String id;

    //购买商品的用户ID
    private Integer userId;

    //购买的商品ID
    private Integer itemId;

    //购买商品的单价，若promoId非空，则表示是以秒杀商品价格
    private BigDecimal itemPrice;

    //购买商品数量
    private Integer amount;

    //购买商品的金额，若promoId非空，则表示是以秒杀商品价格
    private BigDecimal orderAccount;

    //商品优惠券ID
    private Integer promoId;

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

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderAccount() {
        return orderAccount;
    }

    public void setOrderAccount(BigDecimal orderAccount) {
        this.orderAccount = orderAccount;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

}
