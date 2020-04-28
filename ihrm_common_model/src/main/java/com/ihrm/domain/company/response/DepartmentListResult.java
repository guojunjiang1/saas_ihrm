package com.ihrm.domain.company.response;

import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/18 18:41
 */
@Data
@NoArgsConstructor
public class DepartmentListResult {
    private String companyId;
    private String companyName;
    private String companyManage;//公司联系人
    private List<Department> depts;
    public DepartmentListResult(Company company,List<Department> depts){
        this.companyId=company.getId();
        this.companyName=company.getName();
        this.companyManage=company.getLegalRepresentative();
        this.depts=depts;
    }
}
