package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 12:32
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
//角色
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    //为角色添加权限
    @PutMapping("role/assignPrem")
    public Result savePermissions(@RequestBody Map<String,Object> map){
        roleService.savePermissions(map);
        return new Result(ResultCode.SUCCESS);
    }

    //添加角色信息
    @PostMapping("role")
    public Result save(@RequestBody Role role){
        role.setCompanyId(companyId);
        roleService.save(role);
        return new Result(ResultCode.SUCCESS);
    }

    //修改角色信息
    @PutMapping("role/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody Role role){
        role.setId(id);
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询角色
    @GetMapping("role/{id}")
    public Result findById(@PathVariable("id") String id){
        Role role = roleService.findById(id);
        RoleResult roleResult=new RoleResult(role);
        return new Result(ResultCode.SUCCESS,roleResult);
    }

    //分页查询某一企业下的所有角色
    @GetMapping("role")
    public Result findByPage(int page, int pagesize, @RequestParam Map<String,Object> map){
        Page<Role> pages = roleService.findByPage(companyId, page, pagesize);
        PageResult<Role> pageResult=new PageResult<>(pages.getTotalElements(),pages.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    //查询某一企业下的所有角色
    @GetMapping("role/list")
    public Result findAll(){
        List<Role> list = roleService.findAll(companyId);
        return new Result(ResultCode.SUCCESS,list);
    }

    //删除某一角色
    @DeleteMapping("role/{id}")
    public Result delete(@PathVariable("id") String id){
        roleService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
