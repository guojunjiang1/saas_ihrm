package face;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/27 9:56
 */
public class 人脸注册 {
    //人脸注册:向百度的人脸库中添加用户人脸照片
    @Test
    public void testFaceRegister() throws Exception {
        //1.创建java代码和百度云交互的client对象
        //百度云，人脸识别应用里的
        AipFace client = new AipFace("19622227","SZjekuXBmR0NiodOHTU9wKvp","AfOcwKpZs5v1mjlYm83FADX16ohUUesR");
        //2.参数设置
        HashMap<String,String> options = new HashMap<>();
        options.put("quality_control","NORMAL");//图片质量  NONE  LOW  NORMAL，HIGH
        options.put("liveness_control","LOW");//活体检测
        //3.构造图片
        String path = "D:\\新桌面\\office\\WIN_20200427_09_58_39_Pro.jpg";
        //上传的图片  两种格式 ： url地址，Base64字符串形式
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String encode = Base64Util.encode(bytes);
        //4.调用api方法完成人脸注册
        /**
         * 参数一：（图片的url或者图片的Base64字符串），
         * 参数二：图片形式（URL,BASE64）
         * 参数三：组ID（固定字符串）
         * 参数四：用户ID
         * 参数五：hashMap中的基本参数配置
         */
        JSONObject res = client.addUser(encode, "BASE64", "guo", "1005", options);
        System.out.println(res.toString());
    }
}
