package com.ihrm.system.dao;

import com.ihrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 12:32
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {
    User findByMobile(String mobile);
}
