package com.ihrm.common.interceptor;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.ExceptionCast;
import com.ihrm.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/21 11:26
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtils jwtUtils;

    //threadLocal是一个线程局部变量，每个用户发一个请求过来就是一个独立的threadLocal，用它来存储用户信息
    private static final ThreadLocal<Claims> THREAD_LOCAL=new ThreadLocal<>();

    //api执行前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.通过request获取请求token信息（判断是否登录）
        String authorization = request.getHeader("Authorization");
        //判断请求头信息是否为空，或者是否已Bearer开头
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer")) {
            //获取token数据
            String token = authorization.replace("Bearer ", "");
            //解析token获取claims
            Claims claims = jwtUtils.parseJwt(token);
            if (claims != null) {
                //2.判断是否有权限
                //通过handler
                HandlerMethod h = (HandlerMethod) handler;
                //获取接口上的reqeustmapping注解
                RequestMapping annotation = h.getMethodAnnotation(RequestMapping.class);
                //获取当前请求接口中的name属性(权限名)
                String name = annotation.name();
                //判断要访问的方法是否需要权限
                if (!StringUtils.isEmpty(name)) {
                    //通过claims获取到当前用户的可访问API权限字符串
                    String apis = (String) claims.get("apis");  //api-user-delete,api-user-update
                    //判断当前用户是否具有响应的请求权限
                    if (apis.contains(name)) {
                        THREAD_LOCAL.set(claims);
                        return true;
                    } else {
                        ExceptionCast.cast(ResultCode.UNAUTHORISE);
                    }
                }else {
                    THREAD_LOCAL.set(claims);
                    return true;
                }
            }
        }
        ExceptionCast.cast(ResultCode.UNAUTHENTICATED);
        return true;
    }


    //提供一个方法，获取用户信息
    public Claims getClaims(){
        return THREAD_LOCAL.get();
    }

    //方法执行完
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        THREAD_LOCAL.remove();
    }
}
