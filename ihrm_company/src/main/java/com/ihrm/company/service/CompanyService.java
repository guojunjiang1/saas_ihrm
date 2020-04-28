package com.ihrm.company.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/17 15:49
 */
//公司
@Service
@Transactional
public class CompanyService {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private IdWorker idWorker;

    //保存公司信息
    public void add(Company company){
        company.setId(idWorker.nextId()+"");
        company.setAuditState("0");
        company.setState(1);
        company.setCreateTime(new Date());
        companyDao.save(company);
    }

    //修改公司信息
    public void update(Company company){
        Optional<Company> aa = companyDao.findById(company.getId());
        Company company1 = aa.get();
        company1.setName(company.getName());
        company1.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(company1);
    }

    //删除公司信息
    public void delete(String companyId){
        companyDao.deleteById(companyId);
    }

    //根据id查询公司
    public Company findById(String companyId){
        Optional<Company> optional = companyDao.findById(companyId);
        if (!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    //查询公司列表
    public List<Company> findAll(){
        return companyDao.findAll();
    }
}
