package com.mioashaproject.service.impl;

import com.mioashaproject.dao.OrderDOMapper;
import com.mioashaproject.dao.SequenceDOMapper;
import com.mioashaproject.dataobject.OrderDO;
import com.mioashaproject.dataobject.SequenceDO;
import com.mioashaproject.error.BusinessException;
import com.mioashaproject.error.EmBusinessError;
import com.mioashaproject.service.ItemService;
import com.mioashaproject.service.OrderService;
import com.mioashaproject.service.UserService;
import com.mioashaproject.service.model.ItemModel;
import com.mioashaproject.service.model.OrderModel;
import com.mioashaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Huzhimin on 2019/04/21/0021 13:50
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;


    @Override
    @Transactional
    public OrderModel creatOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BusinessException {
        //1.校验下单状态，下单的商品是否存在，用户是否合法，购买数量是否正常
        //根据商品ID查询商品信息和库存
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        //校验用户是否存在异常
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品数量信息不正确(商品数量必须大于0)");
        }

        //校验活动信息
        if (promoId != null){
            //校验对应活动是否存在这个适用商品
            if (promoId.intValue() != itemModel.getPromoModel().getItemId());
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
        }//校验活动是否正在进行中
        else if (itemModel.getPromoModel().getStatus() != 2){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动还未开始");
        }
        //2.落单减库存，（支付减库存）

        Boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        //3.订单入库
        OrderModel orderModel = new OrderModel();
        //用户ID
        orderModel.setUserId(userId);
        //商品ID
        orderModel.setItemId(itemId);
        //购买商品的数量
        orderModel.setAmount(amount);
        //校验商品ID是不是活动促销 商品
        if (promoId != null){
            //优惠券ID
            orderModel.setPromoId(promoId);
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            //购买商品的单价
            orderModel.setItemPrice(itemModel.getPrice());
        }


        //计算订单金额(单价 * 数量)
        orderModel.setOrderAccount(itemModel.getPrice().multiply(new BigDecimal(amount)));

        //生成交易流水号(订单号)
        orderModel.setId(this.generateOrederNo());

        OrderDO orderDO = this.convertFromOrderModel(orderModel);

        orderDOMapper.insertSelective(orderDO);

        //加上商品销量
        itemService.increaseSales(itemId, amount);

        //4.返回前端
        return orderModel;
    }


    //自动生成交易流水号
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrederNo() {
        //订单号有16位
        StringBuilder stringBuilder = new StringBuilder();

        //前8位为时间信息，年月日
        LocalDateTime localDateTime = LocalDateTime.now();
        String nowDate = localDateTime.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        //把时间信息添加到 stringBuilder
        stringBuilder.append(nowDate);

        //中间6位位自增序列
        //获得当前sequence
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());

        //更新步长
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        //将sequence转换为string
        String sequenceStr = String.valueOf(sequence);

        //固定6位长度
        for (int i = 0;i < 6 - sequenceStr.length();i++){
            //不足位数用0填充
            stringBuilder.append(0);
        }
        //将 sequenceStr 拼接上
        stringBuilder.append(sequenceStr);


        //最后2位为分库分表位，暂时写死
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        //将 orderModel 复制到  orderDO 中
        BeanUtils.copyProperties(orderModel, orderDO);
        return orderDO;
    }
}
