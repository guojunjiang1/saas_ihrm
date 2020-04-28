package com.ihrm.company;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/17 11:58
 */
@SpringBootApplication(scanBasePackages = "com.ihrm")
@EntityScan(value = "com.ihrm.domain.company")//扫描common_model下的实体类及它们的Jpa配置
@EnableEurekaClient
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }

    //雪花算法
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

    //Jwt工具类
    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }

    //解决因拦截器产生的Jpa多对多的noSession问题
    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }
}
