package com.mioashaproject.controller;

import com.mioashaproject.error.BusinessException;
import com.mioashaproject.error.EmBusinessError;
import com.mioashaproject.responcse.CommonReturnType;
import com.mioashaproject.service.OrderService;
import com.mioashaproject.service.model.OrderModel;
import com.mioashaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Huzhimin on 2019/04/21/0021 21:23
 */
@RestController
@RequestMapping("/order")
//@CrossOrigin 可实现跨域请求
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", origins = {"*"})
public class OrderController extends BaseController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private OrderService orderService;

    //封装下单请求
    @RequestMapping(value = "/createOrder", consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId",required = false) Integer promoId) throws BusinessException {


        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        //判断用户是否登陆
        if (isLogin == null || !isLogin.booleanValue()) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }

        //获取用户的登陆信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");


        OrderModel orderModel = orderService.creatOrder(userModel.getId(), itemId, promoId, amount);
        return CommonReturnType.create(null);
    }


}
