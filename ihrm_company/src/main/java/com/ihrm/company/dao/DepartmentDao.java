package com.ihrm.company.dao;

import com.ihrm.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/18 16:34
 */
//部门
public interface DepartmentDao extends JpaRepository<Department,String>, JpaSpecificationExecutor<Department> {
    Department findByCodeAndCompanyId(String code,String companyId);
}
