package shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;


/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/21 17:52
 */
public class 登录认证 {
    @Test
    public void test(){
        //1.根据配置文件创建SecurityManagerFactory
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-test-1.ini");
        //2.通过工厂获取SecurityManager
        SecurityManager securityManager = factory.getInstance();
        //3.将SecurityManager绑定到当前运行环境
        SecurityUtils.setSecurityManager(securityManager);
        //4.从当前运行环境中构造subject
        Subject subject = SecurityUtils.getSubject();
        //5.构造shiro登录的数据(模拟用户输入的数据)
        String username = "zhangsan";
        String password = "123456";
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        //6.主体登陆(验证输入的数据和数据库的数据是否相同)
        subject.login(token);
        //7.验证用户是否登录成功
        System.out.println("用户是否登录成功="+subject.isAuthenticated());
        //8.获取登录成功的数据
        System.out.println(subject.getPrincipal());
    }
}
