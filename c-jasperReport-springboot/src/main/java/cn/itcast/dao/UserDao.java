package cn.itcast.dao;

import cn.itcast.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/26 11:33
 */
public interface UserDao extends JpaRepository<User,String> {
}
