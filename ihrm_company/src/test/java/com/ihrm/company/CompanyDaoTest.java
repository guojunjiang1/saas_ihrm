package com.ihrm.company;

import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/17 15:56
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CompanyDaoTest {
    @Autowired
    private CompanyDao companyDao;
    @Test
    public  void test(){
        Company company=new Company();
        company.setName(null);
        System.out.println(company);
    }
}
