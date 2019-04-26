package com.mioashaproject.error;

/**
 * Created by Huzhimin on 2019/04/13/0013 22:20
 */
public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
