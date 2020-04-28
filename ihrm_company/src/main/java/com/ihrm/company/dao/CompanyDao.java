package com.ihrm.company.dao;

import com.ihrm.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/17 15:48
 */
//公司
public interface CompanyDao extends JpaRepository<Company,String>, JpaSpecificationExecutor<Company> {
}
