package com.ihrm.company.service;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DepartmentListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/18 16:35
 */
//部门
@Service
@Transactional
public class DepartmentService extends BaseService<Department> {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private DepartmentDao departmentDao;

    //新增部门
    public void save(Department department){
        department.setId(idWorker.nextId()+"");
        department.setCreateTime(new Date());
        departmentDao.save(department);
    }

    //更新部门信息
    public void update(Department department){
        Department departmentOld = departmentDao.findById(department.getId()).get();
        departmentOld.setCode(department.getCode());
        departmentOld.setIntroduce(department.getIntroduce());
        departmentOld.setName(department.getName());
        departmentDao.save(departmentOld);
    }

    //根据id查询部门
    public Department findById(String id){
        return departmentDao.findById(id).get();
    }

    //查询某一企业下全部部门列表
    public List<Department> findAll(String companyId) {
//        Specification<Department> specification=new Specification<Department>() {
//            @Override
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                Path<Object> companyId1 = root.get("companyId");
//                Predicate equal = criteriaBuilder.equal(companyId1, companyId);
//                return equal;
//            }
//        };
        Specification<Department> specification = getSpe(companyId);
        List<Department> list = departmentDao.findAll(specification);
        return list;
    }

    //删除某一部门
    public void deleteById(String id){
        departmentDao.deleteById(id);
    }

    //根据部门编码查询部门
    public Department findByCode(String code,String companyId) {
        Department department = departmentDao.findByCodeAndCompanyId(code,companyId);
        return department;
    }
}
