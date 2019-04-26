package com.mioashaproject.service.impl;

import com.mioashaproject.dao.ItemDOMapper;
import com.mioashaproject.dao.ItemStockDOMapper;
import com.mioashaproject.dataobject.ItemDO;
import com.mioashaproject.dataobject.ItemStockDO;
import com.mioashaproject.error.BusinessException;
import com.mioashaproject.error.EmBusinessError;
import com.mioashaproject.service.ItemService;
import com.mioashaproject.service.PromoService;
import com.mioashaproject.service.model.ItemModel;
import com.mioashaproject.service.model.PromoModel;
import com.mioashaproject.validator.ValidatotrImpl;
import com.mioashaproject.validator.ValidatotrResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Huzhimin on 2019/04/18/0018 21:15
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private PromoService promoService;

    @Autowired
    private ValidatotrImpl validatotr;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    private ItemDO convertItemDoFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemDO itemDO = new ItemDO();
        //将 itemDO 属性复制给 itemModel
        BeanUtils.copyProperties(itemModel, itemDO);
        //将 itemDO的price BigDecimal强制转换
//        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }

    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        //获取商品信息id
        itemStockDO.setItemId(itemModel.getId());
        //获取商品库存信息
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {

        //入库前校验入参
        ValidatotrResult result = validatotr.validate(itemModel);
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        //转化itemmodel->dataobject
        ItemDO itemDO = this.convertItemDoFromItemModel(itemModel);

        //写入数据库
        itemDOMapper.insertSelective(itemDO);

        //获取商品的 id
        itemModel.setId(itemDO.getId());

        //将获取得到商品信息存入 ItemStockDO
        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);

        //将 ItemStockDO 的信息创入 itemStockDOMapper 执行SQL语句
        itemStockDOMapper.insertSelective(itemStockDO);

        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    //商品信息展示
    @Override
    public List<ItemModel> listitem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();
        //将每一个itemDO stream.map  成itemMode
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> { //使用Java8 stream api
            //查询商品 id
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());

        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        //获取商品信息Id
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null) {
            return null;
        }

        //操作获得库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());

        // 将dataobject -> model
        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);

        //通过 itemId获取活动的商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());

        //判断活动未开始 或者是进行中
        if (promoModel != null && promoModel.getStatus().intValue() != 3) {
            //将活动信息设置在 商品页
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    //库存扣减
    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        //获得受影响的 行数
        int affectedRow = itemStockDOMapper.decreaseStock(itemId,amount);
        //通过受影响的行数判断是否更新成功 affectedRow < 0 表示库存不足但是依旧会执行SQL
        if (affectedRow > 0){
            //更新库存成功
            return true;
        }else {
            //更新库存失败
            return false;
        }
    }

    //商品销量增加计数
    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId,amount);
    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);

//        itemModel.setPrice(new BigDecimal(String.valueOf(itemDO.getPrice())));

        itemModel.setStock(itemStockDO.getStock());

        return itemModel;
    }
}
