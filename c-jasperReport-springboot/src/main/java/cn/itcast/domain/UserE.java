package cn.itcast.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户实体类
 */
public class UserE implements Serializable {
    private Integer count;
    private String company;

    public UserE() {
    }

    public UserE(Integer count, String company) {
        this.count = count;
        this.company = company;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
