package cn.itcast.shiro.controller;

import cn.itcast.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequiresPermissions(value = "user-add")//访问该方法需要拥有的权限
    @RequiresRoles(value = "系统管理员")//访问该方法需要拥有的角色
    @RequestMapping(value = "/user/home")
    public String home() {
        return "访问个人主页成功";
    }

    //添加
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String add() {
        return "添加用户成功";
    }
	
    //查询
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String find() {
        return "查询用户成功";
    }
	
    //更新
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public String update(String id) {
        return "更新用户成功";
    }
	
    //删除
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public String delete() {
        return "删除用户成功";
    }
	
	//用户登录
	@RequestMapping(value="/login")
    public String login(String username,String password) {
        try {
            //一：对密码进行加盐加密(参数：要加密的内容，加密的盐，加密的次数)
            password = new Md5Hash(password, username, 3).toString();
            //二：构建用户令牌
            UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(username,password);
            //三：获取subject
            Subject subject = SecurityUtils.getSubject();
            String sid = (String) subject.getSession().getId();
            //四：调用login方法进行登录认证(里面自动执行自定义的Relam域)
            subject.login(usernamePasswordToken);
            return "登录成功"+sid;
        }catch (Exception e){
            return "用户名或密码错误";
        }
    }

    @RequestMapping("autherror")
    public String took(int code){
        return code==1?"未登录":"未授权";
    }
}
