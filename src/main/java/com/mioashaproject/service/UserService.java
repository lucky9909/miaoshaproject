package com.mioashaproject.service;

import com.mioashaproject.error.BusinessException;
import com.mioashaproject.service.model.UserModel;

/**
 * Created by Huzhimin on 2019/04/12/0012 21:14
 */
public interface UserService {
    //通过用户ID获取对象的方法
    UserModel getUserById(Integer id);

    //用户注册接口方法
    void register(UserModel userModel) throws BusinessException;


    //用户登陆接口方法

    /**
     *
     * @param telphone 用户注册手机
     * @parampassword 用户加密后的密码
     * @throws BusinessException
     */
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;

}
