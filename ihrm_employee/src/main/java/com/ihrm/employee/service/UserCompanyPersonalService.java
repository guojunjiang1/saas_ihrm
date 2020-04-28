package com.ihrm.employee.service;

import com.ihrm.common.poi.ExcelExportUtil;
import com.ihrm.common.utils.DownloadUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.employee.EmployeeResignation;
import com.ihrm.domain.employee.UserCompanyPersonal;
import com.ihrm.domain.employee.response.EmployeeReportResult;
import com.ihrm.employee.dao.EmployeeResignationDao;
import com.ihrm.employee.dao.UserCompanyPersonalDao;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

/**
 */
@Service
public class UserCompanyPersonalService {
    @Autowired
    private UserCompanyPersonalDao userCompanyPersonalDao;
    @Autowired
    private EmployeeResignationDao employeeResignationDao;

    public void save(UserCompanyPersonal personalInfo) {
        userCompanyPersonalDao.save(personalInfo);
    }

    public UserCompanyPersonal findById(String userId) {
        return userCompanyPersonalDao.findByUserId(userId);
    }

    //普通方式导出
    public Workbook export(String companyId, String month) throws Exception{
        //一：构建报表数据(查询本月入职员工的详细数据和本月离职员工的详细数据)
        List<EmployeeReportResult> list = userCompanyPersonalDao.findByMonth(companyId,month+"%");
        //二：构造Excel
        Workbook wb=new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        //构建标题
        String[] strings="编号,姓名,手机,最高学历,国家地区,护照号,籍贯,生日,属相,入职时间,离职类型,离职原因,离职时间".split(",");
        for (int i=0;i<strings.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(strings[i]);
        }
        //构建数据
        Cell cell=null;
        int rowindex=1;
        for (EmployeeReportResult employeeReportResult:list){
            row = sheet.createRow(rowindex++);
            // 编号,
            cell = row.createCell(0);
            cell.setCellValue(employeeReportResult.getUserId());
            // 姓名,
            cell = row.createCell(1);
            cell.setCellValue(employeeReportResult.getUsername());
            // 手机,
            cell = row.createCell(2);
            cell.setCellValue(employeeReportResult.getMobile());
            // 最高学历,
            cell = row.createCell(3);
            cell.setCellValue(employeeReportResult.getTheHighestDegreeOfEducation());
            // 国家地区,
            cell = row.createCell(4);
            cell.setCellValue(employeeReportResult.getNationalArea());
            // 护照号,
            cell = row.createCell(5);
            cell.setCellValue(employeeReportResult.getPassportNo());
            // 籍贯,
            cell = row.createCell(6);
            cell.setCellValue(employeeReportResult.getNativePlace());
            // 生日,
            cell = row.createCell(7);
            cell.setCellValue(employeeReportResult.getBirthday());
            // 属相,
            cell = row.createCell(8);
            cell.setCellValue(employeeReportResult.getZodiac());
            // 入职时间,
            cell = row.createCell(9);
            cell.setCellValue(employeeReportResult.getTimeOfEntry());
            // 离职类型,
            cell = row.createCell(10);
            cell.setCellValue(employeeReportResult.getTypeOfTurnover());
            // 离职原因,
            cell = row.createCell(11);
            cell.setCellValue(employeeReportResult.getReasonsForLeaving());
            // 离职时间
            cell = row.createCell(12);
            cell.setCellValue(employeeReportResult.getResignationTime());
        }
        return wb;
    }

    //指定Excel模板样式导出
    public Workbook export1(String companyId, String month) throws Exception{
        //***获取模板
        //一：加载模板文件
        Resource resource=new ClassPathResource("excelMb/hr-demo.xlsx");
        FileInputStream inputStream=new FileInputStream(resource.getFile());
        //二：根据模板创建工作簿
        Workbook wb=new XSSFWorkbook(inputStream);
        //三：获取模板的样式
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(2);
        CellStyle[] cellStyles=new CellStyle[row.getLastCellNum()];
        for (int i=0;i<row.getLastCellNum();i++){
            Cell cell = row.getCell(i);
            cellStyles[i]=cell.getCellStyle();
        }

        //***要导出的数据
        //四：获取要导出的数据
        List<EmployeeReportResult> list = userCompanyPersonalDao.findByMonth(companyId,month+"%");
        //五：构建Excel数据
        int rowindex=2;
        Cell cell=null;
        for (EmployeeReportResult employeeReportResult:list){
            row = sheet.createRow(rowindex++);
            // 编号,
            cell = row.createCell(0);
            cell.setCellValue(employeeReportResult.getUserId());
            cell.setCellStyle(cellStyles[0]);
            // 姓名,
            cell = row.createCell(1);
            cell.setCellValue(employeeReportResult.getUsername());
            cell.setCellStyle(cellStyles[1]);
            // 手机,
            cell = row.createCell(2);
            cell.setCellValue(employeeReportResult.getMobile());
            cell.setCellStyle(cellStyles[2]);
            // 最高学历,
            cell = row.createCell(3);
            cell.setCellValue(employeeReportResult.getTheHighestDegreeOfEducation());
            cell.setCellStyle(cellStyles[3]);
            // 国家地区,
            cell = row.createCell(4);
            cell.setCellValue(employeeReportResult.getNationalArea());
            cell.setCellStyle(cellStyles[4]);
            // 护照号,
            cell = row.createCell(5);
            cell.setCellValue(employeeReportResult.getPassportNo());
            cell.setCellStyle(cellStyles[5]);
            // 籍贯,
            cell = row.createCell(6);
            cell.setCellValue(employeeReportResult.getNativePlace());
            cell.setCellStyle(cellStyles[6]);
            // 生日,
            cell = row.createCell(7);
            cell.setCellValue(employeeReportResult.getBirthday());
            cell.setCellStyle(cellStyles[7]);
            // 属相,
            cell = row.createCell(8);
            cell.setCellValue(employeeReportResult.getZodiac());
            cell.setCellStyle(cellStyles[8]);
            // 入职时间,
            cell = row.createCell(9);
            cell.setCellValue(employeeReportResult.getTimeOfEntry());
            cell.setCellStyle(cellStyles[9]);
            // 离职类型,
            cell = row.createCell(10);
            cell.setCellValue(employeeReportResult.getTypeOfTurnover());
            cell.setCellStyle(cellStyles[10]);
            // 离职原因,
            cell = row.createCell(11);
            cell.setCellValue(employeeReportResult.getReasonsForLeaving());
            cell.setCellStyle(cellStyles[11]);
            // 离职时间
            cell = row.createCell(12);
            cell.setCellValue(employeeReportResult.getResignationTime());
            cell.setCellStyle(cellStyles[12]);
        }
        return wb;
    }

    //指定Excel模板样式工具类方式导出数据
    public void export2(String companyId, String month,HttpServletResponse response) throws Exception{
        Resource resource=new ClassPathResource("excelMb/hr-demo.xlsx");
        FileInputStream inputStream=new FileInputStream(resource.getFile());
        List<EmployeeReportResult> list = userCompanyPersonalDao.findByMonth(companyId, month + "%");
        //*:要在实体类属性上添加注解，指定当前属性在表中的索引位置
        //参数：要导出数据的实体类，rowindex:从第几行开始添加数据，styleindex：模板样式的在第几行
        //respon的对象，模板的输入流，要导出的数据，导出的表的名称
        new ExcelExportUtil<EmployeeReportResult>(EmployeeReportResult.class,2,2)
                .export(response,inputStream,list,month+"人事报表.xlsx");
    }
}
