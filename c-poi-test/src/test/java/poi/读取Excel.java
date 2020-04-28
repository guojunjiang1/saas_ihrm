package poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/23 17:19
 */
public class 读取Excel {
    public static void main(String[] args) throws Exception{
        //一：获取Excel的工作簿
        Workbook wb=new XSSFWorkbook("D:\\demo.xlsx");
        //二：获取该Excel下的sheet
        Sheet sheet = wb.getSheetAt(0);//指定索引，获取第一个sheet表
        //三：获取每个单元格的数据
        //遍历所有行
        for (int i=0;i<sheet.getLastRowNum()+1;i++){
            Row row = sheet.getRow(i);//获取到行
            //遍历每一行的所有单元格
            for (int j=2;j<row.getLastCellNum();j++){
                Cell cell = row.getCell(j);//获取到单元格
                //excel中每种数据类型的获取方式都不一样
                System.out.print(getCellValue(cell)+" ");
            }
            System.out.println();
        }
    }
    public static Object getCellValue(Cell cell) {
        //1.获取到单元格的属性类型
        CellType cellType = cell.getCellType();
        //2.根据单元格数据类型获取数据
        Object value = null;
        switch (cellType) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)) {
                    //日期格式
                    value = cell.getDateCellValue();
                }else{
                    //数字
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA: //公式
                value = cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }
}
