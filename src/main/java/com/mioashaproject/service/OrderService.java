package com.mioashaproject.service;

import com.mioashaproject.error.BusinessException;
import com.mioashaproject.service.model.OrderModel;

/**
 * Created by Huzhimin on 2019/04/21/0021 13:46
 */
public interface OrderService {
    //通过前端的 url 上传过来秒杀活动，然后下单接口内校验对应id是否属于对应商品且活动已开始
    //创建订单 请求参数的请求体 用户ID 购买的商品ID 购买商品数量
    OrderModel creatOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;

}
