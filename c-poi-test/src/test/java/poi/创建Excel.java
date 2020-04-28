package poi;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/23 11:52
 */
public class 创建Excel {
    public static void main(String[] args) throws Exception{
        //一：获取工作簿
        Workbook work = new XSSFWorkbook();
        //二：创建表单sheet
        Sheet sheet = work.createSheet("poi");
        //三：创建输出流
        FileOutputStream outputStream=new FileOutputStream("d:\\test.xlsx");
        work.write(outputStream);
        outputStream.close();
    }
}
