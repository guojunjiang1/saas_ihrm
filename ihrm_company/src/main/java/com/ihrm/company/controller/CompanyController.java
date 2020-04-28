package com.ihrm.company.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/17 15:50
 */
//公司
@RestController
@RequestMapping("company")
@CrossOrigin
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    //保存公司信息
    @PostMapping()
    public Result add(@RequestBody Company company){
        companyService.add(company);
        return new Result(ResultCode.SUCCESS);
    }

    //修改公司信息
    @PutMapping("{companyId}")
    public Result update(@PathVariable("companyId") String companyId,@RequestBody Company company){
        company.setId(companyId);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }

    //删除公司信息
    @DeleteMapping("{companyId}")
    public Result delete(@PathVariable("companyId") String companyId){
        companyService.delete(companyId);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询公司
    @GetMapping("{companyId}")
    public Result findById(@PathVariable("companyId") String companyId){
        Company company = companyService.findById(companyId);
        if (company==null){
            return new Result(ResultCode.FAIL);
        }
        return new Result(ResultCode.SUCCESS,company);
    }

    //查询公司列表
    @GetMapping
    public Result findAll(){
        List<Company> list = companyService.findAll();
        return new Result(ResultCode.SUCCESS,list);
    }
}
