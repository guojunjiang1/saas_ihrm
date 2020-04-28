package cn.itcast.shiro.realm;

import cn.itcast.shiro.dao.UserDao;
import cn.itcast.shiro.domain.Permission;
import cn.itcast.shiro.domain.Role;
import cn.itcast.shiro.domain.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/22 9:03
 */
//自定义的Realm域
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private UserDao userDao;
    @Override
    public void setName(String name) {
        super.setName("customRelam");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //用户授权
        //一：获取用户信息
        User user = (User) principalCollection.getPrimaryPrincipal();
        //二：获取用户的角色和权限
        Set<Role> sets = user.getRoles();//用户的角色
        Set<String> roles=new HashSet<>();
        Set<String> permissions=new HashSet<>();
        for (Role xx:sets){
            Set<Permission> set = xx.getPermissions();//用户的权限
            for (Permission xxx:set){
                permissions.add(xxx.getCode());
            }
            roles.add(xx.getName());
        }
        //三：存入shiro
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //用户登录认证
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        //一：获取用户输入的账号密码
        String username = upToken.getUsername();
        String password=new String(upToken.getPassword());
        //二：查询数据库。。。
        User user = userDao.findByUsername(username);
        if (user==null||!user.getPassword().equals(password)){
            //用户名密码错误
            return null;
        }
        //三：认证通过，向shiro中存入数据
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,password,getName());
        return info;
    }
}
