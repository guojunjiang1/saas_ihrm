package com.ihrm.common.shiroRealm;

import com.ihrm.domain.system.response.ProfileResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Map;
import java.util.Set;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/22 18:05
 */
//公共的realm域，用于为用户授权
public class IhrmRealm extends AuthorizingRealm {
    @Override
    public void setName(String name) {
        super.setName("ihrmRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //授权
        ProfileResult profileResult = (ProfileResult) principalCollection.getPrimaryPrincipal();
        Map<String, Object> permissions = profileResult.getRoles();
        Set<String> apis =(Set<String>) permissions.get("apis");
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.setStringPermissions(apis);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
