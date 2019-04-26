package com.mioashaproject.service;

import com.mioashaproject.service.model.PromoModel;

/**
 * Created by Huzhimin on 2019/04/23/0023 10:35
 */
public interface PromoService {
    //根据 itemId 获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}
