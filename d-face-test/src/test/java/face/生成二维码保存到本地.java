package face;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/27 11:33
 */
public class 生成二维码保存到本地 {
    @Test
    public void test() throws Exception{
        //1.二维码中的信息(扫描二维码之后跳转的路径)
        String content = "http://www.itcast.cn";
        //2.通过zxing生成二维码(保存到本地图片，支持以data url的形式体现)
        //创建QRCodeWriter对象
        QRCodeWriter writer = new QRCodeWriter();
        //基本配置
        //二维码信息，二维码类型，二维码的宽度，二维码的高度
        BitMatrix bt = writer.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        //保存二维码到本地
        Path path = new File("D:\\test.png").toPath();
        MatrixToImageWriter.writeToPath(bt,"png",path);
    }
}
