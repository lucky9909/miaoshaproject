package com.mioashaproject.error;

/**
 * Created by Huzhimin on 2019/04/13/0013 23:08
 * 包装器业务异常类实现
 */
public class BusinessException extends Exception implements CommonError{

    private CommonError commonError;

    //直接接收 EmBusinessError的传参用于构造业务的异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }


    //接受自定义errMsg的方式构造业务异常
    public BusinessException(CommonError commonError,String ErrMsg){
        super();
        this.commonError = commonError;
        //二次改写ErrMsg
        this.commonError.setErrMsg(ErrMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
