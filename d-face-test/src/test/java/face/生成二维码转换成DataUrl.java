package face;

import com.baidu.aip.util.Base64Util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/27 11:48
 */
public class 生成二维码转换成DataUrl {
    @Test
    public void test() throws Exception{
        //1.二维码中的信息(扫描二维码之后跳转的路径)
        String content = "http://www.itcast.cn";
        //2.通过zxing生成二维码(保存到本地图片，支持以data url的形式体现)
        //创建QRCodeWriter对象
        QRCodeWriter writer = new QRCodeWriter();
        //基本配置
        BitMatrix bt = writer.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        //创建ByteArrayOutputstream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //将二维码数据以byte数组的形式保存到ByteArrayOutputstream
        /**
         * 1：image对象
         * 2：图片格式
         * 3：Outputstream
         */
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bt);
        ImageIO.write(image,"png",os);
        //对byte数组进行base64处理
        String encode = Base64Util.encode(os.toByteArray());
        System.out.println(new String("data:image/png;base64,"+encode));
    }
}
