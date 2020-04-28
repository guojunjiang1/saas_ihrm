package com.ihrm.common.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/19 9:06
 */
public class BaseService<T> {

    //抽取出，根据公司id查询的specification对象
    protected Specification<T> getSpe(String companyId){
        Specification<T> specification=new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<Object> companyId1 = root.get("companyId");
                Predicate equal = criteriaBuilder.equal(companyId1, companyId);
                return equal;
            }
        };
        return specification;
    }
}
