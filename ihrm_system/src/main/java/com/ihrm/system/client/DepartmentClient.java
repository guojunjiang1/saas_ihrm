package com.ihrm.system.client;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.company.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/23 10:33
 */
@FeignClient("ihrm-company")
public interface DepartmentClient {

    //根据id查询部门
    @GetMapping("company/department/{id}")
    Result findById(@PathVariable("id") String id);

    //根据部门编码查询部门
    @PostMapping("company/department/search")
    Department findByCode(@RequestParam String code, @RequestParam String companyId);
}
