package com.mioashaproject.service;

import com.mioashaproject.error.BusinessException;
import com.mioashaproject.service.model.ItemModel;

import java.util.List;

/**
 * Created by Huzhimin on 2019/04/18/0018 21:11
 */
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listitem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount) throws BusinessException;

    //商品销量增加
    void increaseSales(Integer itemId,Integer amount) throws BusinessException;
}
