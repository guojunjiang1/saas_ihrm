package poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/23 11:52
 */
public class 创建Excel并填充数据且设置样式 {
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

        //六：设置样式
        //创建样式对象（将来赋值给某一单元格的）
        CellStyle style = work.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        //创建字体对象
        Font font = work.createFont();
        font.setFontName("华文行楷"); //字体
        font.setFontHeightInPoints((short)28);//字号
        style.setFont(font);
        //居中显示
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //向某一单元格设置样式
        cell.setCellStyle(style);

        //设置某一行和列的行高和列宽
        row.setHeightInPoints(50);//行高
        sheet.setColumnWidth(2,42 * 256);//列宽(指定第三列，从0开始数)

        //七：创建输出流
        FileOutputStream outputStream=new FileOutputStream("d:\\test.xlsx");
        work.write(outputStream);
        outputStream.close();
    }
}
