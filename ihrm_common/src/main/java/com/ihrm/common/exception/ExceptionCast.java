package com.ihrm.common.exception;

import com.ihrm.common.entity.ResultCode;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 8:59
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode){
        throw new MyException(resultCode);
    }
}
