package poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/23 11:52
 */
public class 创建Excel并填充数据 {
    public static void main(String[] args) throws Exception{
        //一：获取工作簿
        Workbook work = new XSSFWorkbook();
        //二：创建表单sheet
        Sheet sheet = work.createSheet("poi");
        //三：创建行(索引从0开始，2表示第三行)
        Row row = sheet.createRow(2);
        //四：创建单元格(索引从0开始，2表示第三个单元格)
        Cell cell = row.createCell(2);
        //五：为单元格赋值
        cell.setCellValue("陪你度过春秋冬夏");
        //六：创建输出流
        FileOutputStream outputStream=new FileOutputStream("d:\\test.xlsx");
        work.write(outputStream);
        outputStream.close();
    }
}
