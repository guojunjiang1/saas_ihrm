package com.ihrm.common.controller;

import com.ihrm.common.interceptor.JwtInterceptor;
import com.ihrm.domain.system.response.ProfileResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 8:52
 */
//提供一些属性
public class BaseController {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    protected String userId;
    protected Claims claims;
    protected ProfileResult profileResult;
    //解析jwt令牌获取用户信息
//    @ModelAttribute
//    public void init(HttpServletRequest request, HttpServletResponse response){
//        this.request=request;
//        this.response=response;
//        Claims claims = jwtInterceptor.getClaims();
//        if (claims!=null) {
//            this.claims=claims;
//            this.companyId = (String) claims.get("companyId");
//            this.companyName = (String) claims.get("companyName");
//        }
//    }
    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        if(principals != null && !principals.isEmpty()) {
            //2.获取安全数据
            ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
            this.companyId = result.getCompanyId();
            this.companyName = result.getCompany();
            this.userId=result.getUserId();
            this.profileResult=result;
        }
    }
}
