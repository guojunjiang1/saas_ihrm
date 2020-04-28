package qinniu;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/25 10:32
 */
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.File;

public class 将图片上传到青牛云 {
    @Test
    public void testUpload01() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        //上传管理器
        UploadManager uploadManager = new UploadManager(cfg);
        //指定公钥私钥，存储空间名称
        String accessKey = "rejtT_MP2Ymmy4nwytfOEH3D-Z0nTfAhXkPwrcbT";
        String secretKey = "KugRqeND8ObzEjZOlkXdhumwITLrgGk8UcDQ-rMB";
        String bucket = "ihrm-images";
        //图片路径
        String localFilePath = "D:\\001.png";
        //存入到存储空间的文件名a
        String key = "test";
        //身份认证
        Auth auth = Auth.create(accessKey, secretKey);
        //指定覆盖上传(第二参数指定要覆盖的文件名即可)
        String upToken = auth.uploadToken(bucket,"test");
        try {
            //上传
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
}
