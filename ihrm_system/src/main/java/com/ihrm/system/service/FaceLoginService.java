package com.ihrm.system.service;


import com.baidu.aip.util.Base64Util;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.FaceLoginResult;
import com.ihrm.domain.system.QRCode;
import com.ihrm.domain.system.User;
import com.ihrm.system.utils.BaiduAiUtil;
import com.ihrm.system.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
public class FaceLoginService {

    @Value("${qr.url}")
    private String url;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private QRCodeUtil qrCodeUtil;
    @Autowired
    private BaiduAiUtil baiduAiUtil;
	@Autowired
    private IdWorker idWorker;
	@Autowired
    private UserService userService;
    //创建二维码
    public QRCode getQRCode() throws Exception {
        String code = idWorker.nextId() + "";//生成一个该二维码的唯一标记
        String QrCode = qrCodeUtil.crateQRCode(url+"?code="+code);//生成二维码
        //当前生成二维码的状态-1未扫描 1扫描成功 0扫描失败
        FaceLoginResult result=new FaceLoginResult("-1");
        //将二维码唯一标识及二维码状态存入redis
        redisTemplate.boundValueOps(getCacheKey(code)).set(result,10, TimeUnit.MINUTES);
        QRCode qrCode=new QRCode();
        qrCode.setFile(QrCode);
        qrCode.setCode(code);
        return qrCode;
    }

	//根据唯一标识，查询当前二维码状态
    public FaceLoginResult checkQRCode(String code) {
        FaceLoginResult result = (FaceLoginResult) redisTemplate.opsForValue().get(getCacheKey(code));
        return result;
    }

	//扫描二维码之后，使用拍摄照片进行登录
    public String loginByFace(String code, MultipartFile attachment) throws Exception {
        byte[] bytes = attachment.getBytes();
        String encode = Base64Util.encode(bytes);
        String userId = baiduAiUtil.faceSearch(encode);
        FaceLoginResult result = (FaceLoginResult) redisTemplate.opsForValue().get(getCacheKey(code));
        if (result!=null) {
            if (StringUtils.isEmpty(userId)) {
                result.setState("-1");//扫描失败
            } else {
                result.setState("1");//扫描成功
                result.setUserId(userId);
                User user = userService.findById(userId);
                if (user != null) {
                    String sessionId = userService.login2(user.getMobile(), user.getPassword());//调用登录接口
                    result.setToken(sessionId);
                }
            }
            redisTemplate.boundValueOps(getCacheKey(code)).set(result, 10, TimeUnit.MINUTES);
        }
        return userId;
    }

    //人脸检测
    public Result checkFace(MultipartFile attachment) throws Exception{
        byte[] bytes = attachment.getBytes();
        String encode = Base64Util.encode(bytes);
        Boolean flag= baiduAiUtil.faceCheck(encode);
        if (flag){
            return new Result(ResultCode.SUCCESS);
        }else {
            return new Result(ResultCode.FAIL);
        }
    }

	//构造缓存key
    private String getCacheKey(String code) {
        return "qrcode_" + code;
    }


}
