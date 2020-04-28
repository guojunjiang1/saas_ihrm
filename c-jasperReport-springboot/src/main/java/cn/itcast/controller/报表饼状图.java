package cn.itcast.controller;

import cn.itcast.domain.User;
import cn.itcast.domain.UserE;
import cn.itcast.service.UserService;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/25 18:19
 */
@RestController
//将PDF报表以饼状图形式显示
public class 报表饼状图 {

    //数据源是JDBC时(并指定条件)
    @GetMapping("test5")
    public void creaetPdf1(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //一：用JasperSoftStudio工具构建出模板
        //二：加载模板(jasper文件)
        Resource resource = new ClassPathResource("template/chartsPdf.jasper");
        FileInputStream inputStream = new FileInputStream(resource.getFile());
        //三：准备数据源。。
        List<UserE> list =new ArrayList<>();
        list.add(new UserE(10,"传智播客"));
        list.add(new UserE(15,"黑马程序员"));
        list.add(new UserE(5,"传智学院"));
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
        Map<String, Object> map = new HashMap<>();
        //四：在模板中填充数据                                                                                                                                                    生成JasperPrint
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, jrBeanCollectionDataSource);
        //五：以PDF形式输出
        ServletOutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        //六：关闭流
        inputStream.close();
        outputStream.close();
    }
}
