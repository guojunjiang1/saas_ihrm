package com.ihrm.system.service;

import com.baidu.aip.util.Base64Util;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.ExceptionCast;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.common.utils.QiniuUploadUtil;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.client.DepartmentClient;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import com.ihrm.domain.system.User;
import com.ihrm.system.utils.BaiduAiUtil;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 12:32
 */
@Service
@Transactional
//用户
public class UserService  {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private DepartmentClient departmentClient;
    @Autowired
    private BaiduAiUtil baiduAiUtil;

    //为用户分配角色
    public void saveRoles(Map<String, Object> map) {
        String id =(String) map.get("id");
        User user = userDao.findById(id).get();
        List<String> strings = (List<String>) map.get("roleIds");
        Set<Role> set=new HashSet<>();
        for (String xx:strings){
            Role role = roleDao.findById(xx).get();
            set.add(role);
        }
        user.setRoles(set);
        userDao.save(user);
    }

    //新增用户
    public void save(User user){
        user.setId(idWorker.nextId()+"");
        user.setPassword("123456");//设置初始密码
        user.setEnableState(1);//开启
        user.setCreateTime(new Date());
        userDao.save(user);
    }

    //更新用户
    public void update(User user){
        User UserOld = userDao.findById(user.getId()).get();
        UserOld.setUsername(user.getUsername());
        UserOld.setPassword(user.getPassword());
        UserOld.setDepartmentId(user.getDepartmentId());
        UserOld.setDepartmentName(user.getDepartmentName());
        userDao.save(UserOld);
    }

    //根据id查询用户
    public User findById(String id){
        return userDao.findById(id).get();
    }

    //查询全部用户
    public Page<User> findAll(Map<String,Object> map,int page,int size) {
        Specification<User> specification=new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> list=new ArrayList<>();
                //根据公司id查询
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    Path<Object> companyId = root.get("companyId");
                    Predicate equal = cb.equal(companyId,(String)map.get("companyId"));
                    list.add(equal);
                }
                //根据部门id查询
                if (!StringUtils.isEmpty(map.get("departmentId"))){
                    Path<Object> departmentId = root.get("departmentId");
                    Predicate equal = cb.equal(departmentId,(String)map.get("departmentId"));
                    list.add(equal);
                }
                //根据hasDept判断是否分配部门，0未分配，1已分配
                if (!StringUtils.isEmpty(map.get("hasDept"))){
                    String hasDept = (String) map.get("hasDept");
                    if ("0".equals(hasDept)){
                        //未分配，部门id为null
                        Path<Object> departmentId = root.get("departmentId");
                        Predicate equal = cb.isNull(departmentId);
                        list.add(equal);
                    }else if ("1".equals(hasDept)){
                        //已分配，部门id不为null的
                        Path<Object> departmentId = root.get("departmentId");
                        Predicate equal = cb.isNotNull(departmentId);
                        list.add(equal);
                    }
                }
                Predicate pre = cb.and(list.toArray(new Predicate[list.size()]));
                return pre;
            }
        };
        if (page<=0){
            page=1;
        }
        if (size<=0){
            size=5;
        }
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<User> pages = userDao.findAll(specification, pageRequest);
        return pages;
    }
    //删除某一用户
    public void deleteById(String id){
         userDao.deleteById(id);
    }

    //用户登录
    public String login(String mobile, String password) {
        if (StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            ExceptionCast.cast(ResultCode.FAIL);
        }
        User user = userDao.findByMobile(mobile);
        if (user==null||!user.getPassword().equals(password)){
            ExceptionCast.cast(ResultCode.FAIL1);
        }
        //手机，密码正确，生成令牌
        Map<String,Object> map = new HashMap<>();
        map.put("companyId",user.getCompanyId());
        map.put("companyName",user.getCompanyName());
        StringBuilder apis=new StringBuilder();
        Set<Role> roles = user.getRoles();
        Map<String,Object> map1=new HashMap<>();
        if ("saasAdmin".equals(user.getLevel())){
            //当前是系统管理员
            map1.put("type","3");
            List<Permission> list = permissionService.findAll(map1);
            for (Permission xx:list){
                apis.append(xx.getCode()).append(",");
            }
        }else if ("coAdmin".equals(user.getLevel())){
            //当前是企业管理员
            map1.put("enVisible","1");
            map1.put("type","3");
            List<Permission> list = permissionService.findAll(map1);
            for (Permission xx:list){
                apis.append(xx.getCode()).append(",");
            }
        }else {
            //当前是企业员工，从数据库获取该用户下的权限
            for (Role xx:roles){
                Set<Permission> permissions = xx.getPermissions();
                for (Permission xxx:permissions){
                    if (xxx.getType()==3){
                        apis.append(xxx.getCode()).append(",");
                    }
                }
            }
        }
        map.put("apis",apis.toString());
        String jwt = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
        return jwt;
    }

    //用户登录，shiro方式
    public String login2(String mobile, String password) {
        try {
            //一：构造UsernamePasswordToken对象
            UsernamePasswordToken up=new UsernamePasswordToken(mobile,password);
            //二：获取Subject对象
            Subject subject = SecurityUtils.getSubject();
            //三：获取SessionId
            String sid = (String) subject.getSession().getId();
            //四：调用login方法进行登录认证(里面自动执行自定义的Relam域的认证方法)
            subject.login(up);
            return sid;
        }catch (Exception e){
            return null;
        }
    }

    //用户登录成功后，根据令牌获取用户信息
    public ProfileResult profile(Claims claims) {
        String userId = claims.getId();
        User user = userDao.findById(userId).get();
        Map<String,Object> map=new HashMap<>();
        //封装用户的3种权限
        if ("saasAdmin".equals(user.getLevel())){
            //当前是系统管理员
            List<Permission> list = permissionService.findAll(map);
            ProfileResult profileResult=new ProfileResult(user,list);
            return profileResult;
        }else if ("coAdmin".equals(user.getLevel())){
            //当前是企业管理员
            map.put("enVisible","1");
            List<Permission> list = permissionService.findAll(map);
            ProfileResult profileResult=new ProfileResult(user,list);
            return profileResult;
        }else {
            //当前是企业员工，从数据库获取该用户下的权限
            ProfileResult profileResult=new ProfileResult(user);
            return profileResult;
        }
    }

    //通过poi将excel中的员工(用户)数据导入到数据库中
    public Result importUser(MultipartFile file, String companyId, String companyName) {
        try {
            //一：获取Excel中的数据
            //1：获取Excel的工作簿
            Workbook wb=new XSSFWorkbook(file.getInputStream());
            //2：获取该Excel下的sheet
            Sheet sheet = wb.getSheetAt(0);//指定索引，获取第一个sheet表
            //3：获取每个单元格的数据
            List<User> list=new ArrayList<>();
            //遍历所有行
            for (int i=1;i<sheet.getLastRowNum()+1;i++){
                Row row = sheet.getRow(i);//获取到行
                Object[] objects=new Object[row.getLastCellNum()];
                //遍历每一行的所有单元格
                for (int j=1;j<row.getLastCellNum();j++){
                    Cell cell = row.getCell(j);//获取到单元格
                    //获取到单元格的数据
                    Object value = getCellValue(cell);
                    objects[j-1]=value;
                }
                //二：构造员工数据
                String code=(String)objects[row.getLastCellNum()-2];//根据部门编码查询对应部门
                Department department = departmentClient.findByCode(code, companyId);
                Object data = department.getId();
                Object name = department.getName();
                objects[row.getLastCellNum()-2]=data;
                objects[row.getLastCellNum()-1]=name;
                User user=new User(objects);
                user.setCompanyId(companyId);
                user.setCompanyName(companyName);
                user.setId(idWorker.nextId()+"");
                user.setPassword("123456");
                user.setEnableState(1);
                user.setLevel("user");
                user.setCreateTime(new Date());
                list.add(user);
            }
            //三：批量保存员工
            userDao.saveAll(list);
            return new Result(ResultCode.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.FAIL);
        }
    }

    //获取单元格数据
    //excel中每种数据类型的获取方式都不一样
    public static Object getCellValue(Cell cell) {
        //1.获取到单元格的属性类型
        CellType cellType = cell.getCellType();
        //2.根据单元格数据类型获取数据
        Object value = null;
        switch (cellType) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)) {
                    //日期格式
                    value = cell.getDateCellValue();
                }else{
                    //数字
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA: //公式
                value = cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }

    //通过DataUrl的方式完成图片上传
    //直接把图片信息嵌入到Url中
    public String upload(String id, MultipartFile file) {
        User user = userDao.findById(id).get();
        //将图片的信息以Base64的方式嵌入到URL中
        String url=null;
        try {
            String encode = Base64.encode(file.getBytes());
            url="data:image/png;base64,"+encode;
            user.setStaffPhoto(url);
            userDao.save(user);
        } catch (IOException e) {
            return null;
        }
        return url;
    }

    //将图片上传至七牛云
    public String upload1(String id, MultipartFile file) {
        User user = userDao.findById(id).get();
        String url=null;
        try {
            url= new QiniuUploadUtil().upload(id, file.getBytes());
            user.setStaffPhoto(url);
            userDao.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    //将图片上传到百度云人脸库
    public void upload3(String id, MultipartFile file) throws Exception{
        boolean flag = baiduAiUtil.faceExit(id);
        if (flag){
            //已上传过 人脸更新
            baiduAiUtil.faceUpdate(id, Base64Util.encode(file.getBytes()));
        }else {
            //第一次上传 人脸注册
            baiduAiUtil.faceRegister(id,Base64Util.encode(file.getBytes()));
        }
    }
}
