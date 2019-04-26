package com.mioashaproject.error;

/**
 * Created by Huzhimin on 2019/04/13/0013 22:46
 */
public enum EmBusinessError implements CommonError {
    //通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),

    UNKNOWN_ERROR(10002,"未知错误..."),

    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001, "用户不存在"),

    USER_NOT_EXIST_ITEM(20002, "商品信息不存在"),

    USER_NOT_LOGIN(20003, "账号未登陆，不能下单"),

    //登陆错误定义
    USER_LOGIN_FAIL(20001, "用户手机号或密码不正确"),

    //30000开头为交易信息错误
    STOCK_NOT_ENOUGH(30001,"库存不足，去看看其他商品吧"),

    ;

    EmBusinessError(int ErrCode, String ErrMsg) {
        this.ErrCode = ErrCode;
        this.ErrMsg = ErrMsg;
    }

    private int ErrCode;

    private String ErrMsg;


    @Override
    public int getErrCode() {
        return this.ErrCode;
    }

    @Override
    public String getErrMsg() {
        return this.ErrMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.ErrMsg = errMsg;
        return this;
    }}
