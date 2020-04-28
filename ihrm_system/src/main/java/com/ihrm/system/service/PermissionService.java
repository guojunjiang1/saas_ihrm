package com.ihrm.system.service;

import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.*;
import com.ihrm.system.dao.PermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/20 8:48
 */
@Service
@Transactional
public class PermissionService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionApiDao permissionApiDao;
    @Autowired
    private PermissionMenuDao permissionMenuDao;
    @Autowired
    private PermissionPointDao permissionPointDao;
    
    //新增权限
    //type：1为菜单权限 2为功能权限 3为API权限
    public void save(Map<String,Object> map) throws Exception{
        Integer type = (Integer) map.get("type");
        long id = idWorker.nextId();
        //将map中的属性转为对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id+"");
        if (type!=null) {
            if (type == PermissionConstants.PY_MENU) {
                //菜单权限
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(id + "");
                permissionMenuDao.save(permissionMenu);
            } else if (type == PermissionConstants.PY_POINT) {
                //按钮权限
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(id + "");
                permissionPointDao.save(permissionPoint);
            } else if (type == PermissionConstants.PY_API) {
                //Api权限
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(id + "");
                permissionApiDao.save(permissionApi);
            }
        }
        permissionDao.save(permission);
    }

    //更新权限
    public void update(Map<String,Object> map) throws Exception{
        Integer type = (Integer) map.get("type");
        String id = (String) map.get("id");
        Permission permissionNew = BeanMapUtils.mapToBean(map, Permission.class);
        Permission permission = permissionDao.findById(id).get();
        if (type!=null) {
            if (type == PermissionConstants.PY_MENU) {
                //菜单权限
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(id);
                permissionMenuDao.save(permissionMenu);
            } else if (type == PermissionConstants.PY_POINT) {
                //按钮权限
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(id);
                permissionPointDao.save(permissionPoint);
            } else if (type == PermissionConstants.PY_API) {
                //Api权限
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(id);
                permissionApiDao.save(permissionApi);
            }
        }
        permission.setName(permissionNew.getName());
        permission.setCode(permissionNew.getCode());
        permission.setDescription(permissionNew.getDescription());
        permission.setEnVisible(permissionNew.getEnVisible());
        permissionDao.save(permission);
    }

    //根据id查询权限
    public Map<String, Object> findById(String id){
        Permission permission = permissionDao.findById(id).get();
        Integer type = permission.getType();
        Map<String, Object> map1 = BeanMapUtils.beanToMap(permission);
        map1.remove("id");
        if (type!=null) {
            if (type == PermissionConstants.PY_MENU) {
                //菜单权限
                PermissionMenu permissionMenu = permissionMenuDao.findById(id).get();
                Map<String, Object> map = BeanMapUtils.beanToMap(permissionMenu);
                map1.putAll(map);
            } else if (type == PermissionConstants.PY_POINT) {
                //按钮权限
                PermissionPoint permissionPoint = permissionPointDao.findById(id).get();
                Map<String, Object> map = BeanMapUtils.beanToMap(permissionPoint);
                map1.putAll(map);
            } else if (type == PermissionConstants.PY_API) {
                //Api权限
                PermissionApi permissionApi = permissionApiDao.findById(id).get();
                Map<String, Object> map = BeanMapUtils.beanToMap(permissionApi);
                map1.putAll(map);
            }
        }
        return map1;
    }

    //查询全部权限
    //map中的参数：type:根据权限类型查询 pid:根据父id查询 enVisble:权限私密程度，0：私密的权限 1：企业能看到的权限
    public List<Permission> findAll(Map<String,Object> map){
        String type = (String) map.get("type");
        String pid = (String) map.get("pid");
        String enVisible = (String) map.get("enVisible");
        Specification<Permission> specification=new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list=new ArrayList<>();
                if(!StringUtils.isEmpty(type)){
                    int t = Integer.parseInt(type);
                    //根据权限类型
                    if (t==0){
                        //0:菜单+按钮(in范围查询)
                        Path<Object> type1 = root.get("type");
                        CriteriaBuilder.In<Object> in = cb.in(type1);
                        in.value(1).value(2);
                        list.add(in);
                    }else {
                        Path<Object> type1 = root.get("type");
                        Predicate equal = cb.equal(type1, t);
                        list.add(equal);
                    }
                }
                if (!StringUtils.isEmpty(pid)){
                    //根据父id
                    Path<Object> pid1 = root.get("pid");
                    Predicate equal = cb.equal(pid1, pid);
                    list.add(equal);
                }
                if (!StringUtils.isEmpty(enVisible)){
                    //根绝私密级别
                    Path<Object> enVisible1 = root.get("enVisible");
                    Predicate equal = cb.equal(enVisible1, enVisible);
                    list.add(equal);
                }
                Predicate and = cb.and(list.toArray(new Predicate[list.size()]));
                return and;
            }
        };
        List<Permission> list = permissionDao.findAll(specification);
        return list;
    }

    //删除某一权限，顺便删除权限下的资源
    public void deleteById(String id) {
        Permission permission = permissionDao.findById(id).get();
        Integer type = permission.getType();
        if (type != null) {
            if (type == PermissionConstants.PY_MENU) {
                //菜单权限
                permissionMenuDao.deleteById(id);
            } else if (type == PermissionConstants.PY_POINT) {
                //按钮权限
                permissionPointDao.deleteById(id);
            } else if (type == PermissionConstants.PY_API) {
                //Api权限
                permissionApiDao.deleteById(id);
            }
            permissionDao.deleteById(id);
        }
    }
}
