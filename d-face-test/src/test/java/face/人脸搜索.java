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
 * @date 2020/4/27 10:40
 */
public class 人脸搜索 {
    //上传一张照片和百度云人脸库中的照片一一校验
    //返回一个或多个相似度较高的人脸数据 分数
    //当分数大于80时，我们可以认为是一个人
    @Test
    public void testFaceSearch() throws Exception {
        //1.创建java代码和百度云交互的client对象
        //百度云，人脸识别应用里的
        AipFace client = new AipFace("19622227","","");
        //构造图片
        String path = "D:\\新桌面\\office\\WIN_20200427_09_52_16_Pro.jpg";
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String image = Base64Util.encode(bytes);
        //人脸搜索
        JSONObject res = client.search(image, "BASE64", "guo", null);
        System.out.println(res.toString(2));
    }
}
