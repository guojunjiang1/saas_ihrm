package cn.itcast.domain;


import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户实体类
 */
@Entity
@Table(name = "bs_user")
public class User implements Serializable {
    private static final long serialVersionUID = 4297464181093070302L;
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户名称
     */
    private String username;


    private String companyId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", username='" + username + '\'' +
                ", companyId='" + companyId + '\'' +
                '}';
    }
}
