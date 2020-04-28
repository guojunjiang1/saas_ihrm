package com.ihrm.system.service;

import com.ihrm.common.entity.PageResult;
import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.*;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 12:32
 */
@Service
@Transactional
//角色
public class RoleService extends BaseService<Role> {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    //为角色添加权限
    //如果添加的是菜单权限，则把菜单权限下的按钮权限和api权限都添加
    //如果是按钮权限，则把按钮权限下的api权限都添加
    public void savePermissions(Map<String, Object> map) {
        String id = (String) map.get("id");
        Role role = roleDao.findById(id).get();
        Set<Permission> set=new HashSet<>();
        List<String> permIds = (List<String>) map.get("permIds");
        for (String xx:permIds){
            Permission permission = permissionDao.findById(xx).get();
            String id1 = permission.getId();
            List<Permission> list = permissionDao.findByPidAndType(id1, PermissionConstants.PY_API);
            set.addAll(list);
            set.add(permission);
        }
        role.setPermissions(set);
        roleDao.save(role);
    }

    //新增角色
    public void save(Role role){
        role.setId(idWorker.nextId()+"");
        roleDao.save(role);
    }

    //更新角色
    public void update(Role role){
        Role roleOld = roleDao.findById(role.getId()).get();
        roleOld.setName(role.getName());
        roleOld.setDescription(role.getDescription());
        roleDao.save(roleOld);
    }

    //根据id查询角色
    public Role findById(String id){
        return roleDao.findById(id).get();
    }

    //分页查询全部角色
    public Page<Role> findByPage(String companyId,int page,int size) {
        Specification<Role> specification = getSpe(companyId);
        PageRequest of = PageRequest.of(page - 1, size);
        Page<Role> pages = roleDao.findAll(specification, of);
        return pages;
    }

    //查询全部角色
    public List<Role> findAll(String companyId){
        Specification<Role> spe = getSpe(companyId);
        return roleDao.findAll(spe);
    }

    //删除某一角色
    public void deleteById(String id){
        roleDao.deleteById(id);
    }


}
