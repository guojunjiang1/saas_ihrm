package face;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/27 10:19
 */
public class 人脸检测 {
    //人脸检测：检测上传的照片是否是人的照片
    @Test
    public void testFaceCheck() throws Exception {
        //1.创建java代码和百度云交互的client对象
        //百度云，人脸识别应用里的
        AipFace client = new AipFace("","","");
        //构造图片
        String path = "D:\\新桌面\\office\\44C18BCFC2418A5AC2764B89879B1B32.png";
        //上传的图片  两种格式 ： url地址，Base64字符串形式
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String image = Base64Util.encode(bytes);

        //调用api方法进行人脸检测
        //参数一：（图片的url或者图片的Base64字符串），
        //参数二：图片形式（URL,BASE64）
        //参数三：hashMap中的基本参数配置（null：使用默认配置）
        JSONObject res = client.detect(image, "BASE64", null);
        System.out.println(res.toString(2));
    }
}
