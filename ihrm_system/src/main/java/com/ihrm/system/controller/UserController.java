package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.poi.ExcelImportUtil;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.client.DepartmentClient;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 12:32
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
//用户
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentClient departmentClient;

    //用户图片上传
    @PostMapping("/user/upload/{id}")
    public Result upload(@PathVariable("id") String id,@RequestParam("file") MultipartFile file) throws Exception{
//        String dataUrl=userService.upload(id,file);//dataUrl方法
        String dataUrl = userService.upload1(id, file);//上传到七牛云
        userService.upload3(id,file);//上传到百度云人脸库
        return new Result(ResultCode.SUCCESS,dataUrl);
    }

    //通过poi将excel中的员工(用户)数据导入到数据库中
    @PostMapping("user/import")
    public Result importUser(@RequestParam(name = "file")MultipartFile file){
        return userService.importUser(file,companyId,companyName);
        // 工具类形式导入数据，参数：封装数据的实体类，excel的输入流，从第几行开始，从第几个单元格开始
        // new ExcelImportUtil(User.class).readExcel(inputstrem,x,x);
    }

    //测试feign
    @GetMapping("test/{id}")
    public Result test(@PathVariable("id") String id){
        Result result = departmentClient.findById(id);
        return result;
    }

    //为用户分配角色
    @PutMapping("user/assignRoles")
    public Result saveRoles(@RequestBody Map<String,Object> map){
        userService.saveRoles(map);
        return new Result(ResultCode.SUCCESS);
    }

    //添加用户信息
    @PostMapping("user")
    public Result save(@RequestBody User user){
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.save(user);
        return new Result(ResultCode.SUCCESS);
    }

    //修改用户信息
    @PutMapping("user/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody User user){
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询用户
    @GetMapping("user/{id}")
    public Result findById(@PathVariable("id") String id){
        User user = userService.findById(id);
        UserResult userResult=new UserResult(user);
        return new Result(ResultCode.SUCCESS,userResult);
    }

    //查询某一企业下的所有用户
    @GetMapping("user")
    public Result findAll(int page, int size, @RequestParam Map<String,Object> map){
        map.put("companyId",companyId);
        Page<User> pages = userService.findAll(map, page, size);
        PageResult<User> pageResult=new PageResult<>(pages.getTotalElements(),pages.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    //删除某一用户
    @DeleteMapping("user/{id}")
    public Result delete(@PathVariable("id") String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //用户登录
    @PostMapping("login")
    public Result login(@RequestBody Map<String,String> map){
        String mobile = map.get("mobile");
        String password = map.get("password");
        //shiro方式
        String sessionId=userService.login2(mobile,password);
        if (sessionId==null){
            return new Result(ResultCode.FAIL1);
        }
        return new Result(ResultCode.SUCCESS,sessionId);
        //jwt方式
        //String token=userService.login(mobile,password);
        //return new Result(ResultCode.SUCCESS,token);
    }

    //用户登录成功后，根据令牌获取用户信息
    @PostMapping("profile")
    public Result profile(){
        //jwt解析token获取用户信息
        //ProfileResult profileResult=userService.profile(claims);
        //return new Result(ResultCode.SUCCESS,profileResult);

        return new Result(ResultCode.SUCCESS,profileResult);
    }
}
