package qinniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/25 11:27
 */
public class 断点续传到青牛云 {
    //断点续传
    @Test
    public void testUpload02() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        //...生成上传凭证，然后准备上传
        String accessKey = "-";
        String secretKey = ";
        String bucket = "ihrm-images";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:\\001.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "test1";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        //断点续传：
        String localTempDir = Paths.get(System.getProperty("java.io.tmpdir"), bucket).toString();
        //零时文件保存的路径
        System.out.println(localTempDir);
        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
