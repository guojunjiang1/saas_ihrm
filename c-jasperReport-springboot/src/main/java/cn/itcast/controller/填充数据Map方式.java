package cn.itcast.controller;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/25 18:19
 */
@RestController
//填充数据有两种：一种是map类型的，一种是数据源类型
public class 填充数据Map方式 {

    //填充Map类型的数据
    @GetMapping("test1")
    public void creaetPdf(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //一：用JasperSoftStudio工具构建出模板
        //二：加载模板(jasper文件)
        Resource resource=new ClassPathResource("template/Map.jasper");
        FileInputStream inputStream=new FileInputStream(resource.getFile());
        //三：准备数据。。
        Map<String,Object> map=new HashMap();
        map.put("username","郭峻江");
        map.put("mobile","15386800623");
        map.put("company","阿里巴巴");
        map.put("dept","研发部");
        //四：在模板中填充数据生成JasperPrint
        JasperPrint jasperPrint= JasperFillManager.fillReport(inputStream,map,new JREmptyDataSource());
        //五：以PDF形式输出
        ServletOutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
        //六：关闭流
        inputStream.close();
        outputStream.close();
    }
}
