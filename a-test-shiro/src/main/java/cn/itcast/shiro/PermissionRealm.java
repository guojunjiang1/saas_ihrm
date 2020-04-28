package cn.itcast.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/21 18:59
 */
//通过查询数据库的方式进行用户的认证与校验
public class PermissionRealm extends AuthorizingRealm {
    public void setName(String name){
        //设置relam域名称
        super.setName("PermissionRealm");
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //用户授权
        //一：获取用户信息
        String username = (String) principalCollection.getPrimaryPrincipal();
        //二：模拟数据库获取用户的角色和权限
        //...findByName(username)
        List<String> roles=new ArrayList<String>();
        roles.add("role1");
        roles.add("role2");
        List<String> users=new ArrayList<String>();
        users.add("user:save");
        users.add("user:update");
        //三：存入shiro
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.addRoles(roles);
        info.addStringPermissions(users);
        return info;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //登录认证
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        //一：获取用户输入的账号密码
        String username = upToken.getUsername();
        String password=new String(upToken.getPassword());
        //二：与数据库中的数据做校验
        //...xxx.findByUsername
        if (!password.equals("123456")){
            //用户名或密码输入错误......
            //抛出异常
            throw new RuntimeException("用户名或密码输入错误");
        }else {
            //三：认证通过，向shiro中存入数据
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(username,password,getName());
            return info;
        }
    }
}
