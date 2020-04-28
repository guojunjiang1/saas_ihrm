package com.ihrm.system.dao;

import com.ihrm.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 18:21
 */
public interface RoleDao extends JpaRepository<Role,String>, JpaSpecificationExecutor<Role> {
}
