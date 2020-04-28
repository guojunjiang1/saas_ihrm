package com.ihrm.common.exception;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.message.AuthException;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 9:01
 */
@ControllerAdvice
public class ExceptionCatch {

    //处理自定义抛出的异常
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result myException(MyException myException){
        return new Result(myException.getResultCode());
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public Result a(AuthorizationException myException){
        return new Result(ResultCode.UNAUTHORISE);
    }

    //处理程序抛出的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception exception){
        if (exception.getClass()== AuthorizationException.class){
            return new Result(ResultCode.UNAUTHORISE);
        }
        return new Result(ResultCode.SERVER_ERROR);
    }
}
