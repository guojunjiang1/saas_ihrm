package cn.itcast.shiro.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/22 15:52
 */
@ControllerAdvice
public class ExceptionCatch {
    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    public String error(HttpServletRequest request, HttpServletResponse response, AuthorizationException e) {
        return "未授权";
    }
}
