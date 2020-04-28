package com.ihrm.system.ShiroRealm;

import com.ihrm.common.shiroRealm.IhrmRealm;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.dao.UserDao;
import com.ihrm.system.service.PermissionService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/22 18:08
 */
//用户登录认证的
public class UserRealm extends IhrmRealm {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionService permissionService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //登录认证
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String mobile = upToken.getUsername();
        String password = new String(upToken.getPassword());
        User user = userDao.findByMobile(mobile);
        if (user!=null&&password.equals(user.getPassword())){
            //构造用户(和之前jwt登录后根据用户级别获取用户信息的方法一样)
            //封装用户的3种权限
            ProfileResult profileResult=null;
            Map<String,Object> map=new HashMap<>();
            if ("saasAdmin".equals(user.getLevel())){
                //当前是系统管理员
                List<Permission> list = permissionService.findAll(map);
                 profileResult=new ProfileResult(user,list);
            }else if ("coAdmin".equals(user.getLevel())){
                //当前是企业管理员
                map.put("enVisible","1");
                List<Permission> list = permissionService.findAll(map);
                profileResult=new ProfileResult(user,list);
            }else {
                //当前是企业员工，从数据库获取该用户下的权限
                profileResult=new ProfileResult(user);
            }
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(profileResult,password,getName());
            return info;
        }
        return null;
    }
}
