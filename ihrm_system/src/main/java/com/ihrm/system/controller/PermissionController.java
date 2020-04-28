package com.ihrm.system.controller;

import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Permission;
import com.ihrm.system.service.PermissionService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/20 8:48
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
//权限
//参数用Map封装的原因是，权限对应的有三个类型，不知道具体的类型故用map
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    //添加权限信息
    @PostMapping("permission")
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        System.out.println("添加权限"+map);
        permissionService.save(map);
        return new Result(ResultCode.SUCCESS);
    }

    //修改权限信息
    @PutMapping("permission/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody Map<String,Object> map) throws Exception{
        map.put("id",id);
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询权限
    @GetMapping("permission/{id}")
    public Result findById(@PathVariable("id") String id){
        Map<String,Object> map = permissionService.findById(id);
        return new Result(ResultCode.SUCCESS,map);
    }

    @RequiresPermissions("setPer")
    //查询权限
    @GetMapping(value = "permission",name = "setPer")
    public Result findAll(@RequestParam Map<String,Object> map){
        List<Permission> list = permissionService.findAll(map);
        return new Result(ResultCode.SUCCESS,list);
    }

    //删除某一权限
    @DeleteMapping("permission/{id}")
    public Result delete(@PathVariable("id") String id){
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
