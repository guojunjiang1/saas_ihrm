package com.ihrm.common.exception;

import com.ihrm.common.entity.ResultCode;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 8:57
 */
public class MyException extends RuntimeException{
    private ResultCode resultCode;
    public MyException(ResultCode resultCode){
        this.resultCode=resultCode;
    }
    public ResultCode getResultCode(){
        return this.resultCode;
    }
}
