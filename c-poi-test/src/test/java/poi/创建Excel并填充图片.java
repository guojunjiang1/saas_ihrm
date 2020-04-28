package poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/23 11:52
 */
public class 创建Excel并填充图片 {
    public static void main(String[] args) throws Exception{
        //一：获取工作簿
        Workbook work = new XSSFWorkbook();
        //二：创建表单sheet
        Sheet sheet = work.createSheet("poi");
        //三：填充图片
        //读取图片流
        FileInputStream inputStream=new FileInputStream("F:\\Eclipse SE学习版\\成绩管理系统\\0.jpg");
        //转化二进制数组
        byte[] bytes = IOUtils.toByteArray(inputStream);
        inputStream.read(bytes);
        //向POI内存中添加一张图片，返回图片在图片集合中的索引
        int index = work.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        //绘制图片工具类
        CreationHelper helper = work.getCreationHelper();
        //创建一个绘图对象
        Drawing<?> patriarch = sheet.createDrawingPatriarch();
        //创建锚点，设置图片坐标
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setRow1(0);
        anchor.setCol1(5);
        //绘制图片
        Picture picture = patriarch.createPicture(anchor, index);//图片位置，图片的索引
        picture.resize();//自适应渲染图片
        //四：创建输出流
        FileOutputStream outputStream=new FileOutputStream("d:\\test.xlsx");
        work.write(outputStream);
        outputStream.close();
    }
}
