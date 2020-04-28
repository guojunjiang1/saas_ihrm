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
import java.util.HashMap;
import java.util.Map;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/25 18:19
 */
@RestController
public class 快速入门 {

    @GetMapping("test")
    public void creaetPdf(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //一：用JasperSoftStudio工具构建出模板
        //二：加载模板(jasper文件)
        Resource resource=new ClassPathResource("template/test.jasper");
        FileInputStream inputStream=new FileInputStream(resource.getFile());
        //三：准备数据。。
        Map map=new HashMap();
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
