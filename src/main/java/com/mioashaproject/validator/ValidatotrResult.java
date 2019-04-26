package com.mioashaproject.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Huzhimin on 2019/04/16/0016 22:28
 */
public class ValidatotrResult {
    //校验结果是否有错 带上默认值 就不会有空指针错误
    private  boolean hasErrors = false;

    //存放错误信息的Map
   private Map<String,String> errorMsgMap = new HashMap<>();

    //实现通用的通过格式化字符串信息获取错误结果的msg方法
    public String getErrMsg(){
        //数据拼接
        return StringUtils.join(errorMsgMap.values().toArray(),",");
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }
}
