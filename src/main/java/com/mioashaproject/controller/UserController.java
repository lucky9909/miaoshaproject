package com.mioashaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.mioashaproject.controller.viewobject.UserVO;
import com.mioashaproject.error.BusinessException;
import com.mioashaproject.error.EmBusinessError;
import com.mioashaproject.responcse.CommonReturnType;
import com.mioashaproject.service.UserService;
import com.mioashaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * Created by Huzhimin on 2019/04/12/0012 21:10
 */
@RestController
@Controller("user")
@RequestMapping("/user")
//@CrossOrigin 可实现跨域请求
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*",origins = {"*"})
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",consumes ={CONTENT_TYPE_FORMED} )
    //解析json数据
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomint = random.nextInt(99999);
        randomint += 10000;
        String otpCode = String.valueOf(randomint);


        //将OTP验证码同用户的手机号关联,使用HttpSession方式绑定用户手机号与OTPCODE
        httpServletRequest.getSession().setAttribute(telphone,otpCode);


        //将OTP验证码通过短信通道发送给用户
        System.out.println("电话号码：" + telphone + " 验证码：" + otpCode);
        return CommonReturnType.create(null);
    }


    //用户注册接口
    @RequestMapping(value = "/register",consumes ={CONTENT_TYPE_FORMED} )
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Byte gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证用户的手机号和对应的otpCode相符合(自动生成的otp与发送给用户的otp进行比对)
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if (!StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不正确");
        }

        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        //强制转换为 int 类型
        userModel.setGender((byte) gender.intValue());
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        //用户的密码使用 EncodeByMd5加密方式存入数据库
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);

        return CommonReturnType.create(null);

    }





    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }


    //用户登陆接口
    @RequestMapping(value = "/login",consumes ={CONTENT_TYPE_FORMED} )
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登陆服务,用来校验用户登陆是否合法
        UserModel userModel = userService.validateLogin(telphone,EncodeByMd5(password));

        //将登陆凭证加入到用户登陆成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);

    }



    @RequestMapping(value = "/getuser/{id}")
    //解析json数据
    @ResponseBody
    public CommonReturnType getUser(@PathVariable("id") Integer id) throws BusinessException {
        //调用service服务获取对应的id的用户对象返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }


}
