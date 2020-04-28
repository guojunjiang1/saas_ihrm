package com.ihrm.company.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DepartmentListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/18 16:35
 */
@CrossOrigin
@RestController
@RequestMapping("company")
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService deportmentService;
    @Autowired
    private CompanyService companyService;

    //根据部门编码查询部门
    @PostMapping("department/search")
    public Department findByCode(String code,String companyId){
        return deportmentService.findByCode(code,companyId);
    }

    //添加部门信息
    @PostMapping("department")
    public Result save(@RequestBody Department department){
        department.setCompanyId(companyId);
        deportmentService.save(department);
        return new Result(ResultCode.SUCCESS);
    }

    //修改部门信息
    @PutMapping("department/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody Department department){
        department.setId(id);
        deportmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询部门
    @GetMapping("department/{id}")
    public Result findById(@PathVariable("id") String id){
        Department department = deportmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }

    //查询某一企业下的所有部门
    @GetMapping("department")
    public Result findAll(){
        List<Department> list = deportmentService.findAll(companyId);
        Company company = companyService.findById(companyId);
        if (company==null){
            return new Result(ResultCode.SUCCESS);
        }
        DepartmentListResult departmentListResult=new DepartmentListResult(company,list);
        return new Result(ResultCode.SUCCESS,departmentListResult);
    }

    //删除某一部门
    @DeleteMapping("department/{id}")
    public Result delete(@PathVariable("id") String id){
        deportmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
