package cn.itcast.service;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/26 11:32
 */
@Transactional
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    public List<User> findByCompanyId(String companyId){
        User user=new User();
        ExampleMatcher matching= ExampleMatcher.matching();
        if (!StringUtils.isEmpty(companyId)){
            user.setCompanyId(companyId);
        }
        Example<User> of = Example.of(user, matching);
        return userDao.findAll(of);
    }
}
