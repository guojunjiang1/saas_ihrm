package cn.itcast.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/22 15:26
 */
public class test {
    @Test
    public void testa(){
        System.out.println(new Md5Hash("123456", "wangwu", 3).toString());
    }
}
